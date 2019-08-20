package com.wpj.test.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author wangpejian
 * @date 19-8-16 下午3:25
 */
@RestController
@RequestMapping("kafka")
public class TestKafkaController {

    private int i = 0;

    private AdminClient client;

    private String brokers = "127.0.0.1:9201,127.0.0.1:9202,127.0.0.1:9203,127.0.0.1:9204,127.0.0.1:9205";

    @RequestMapping("send")
    public String send(@RequestParam("num") int num, @RequestParam("topic") String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 设置分区器
        props.put("partitioner.class", "com.wpj.test.config.KafkaPartitioner");

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i1 = 0; i1 < num; i1++) {

            Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, Integer.toString(i), Integer.toString(i)));
            i++;
            try {
                RecordMetadata res = send.get();
                System.out.println(res.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        producer.close();

        return "ok";
    }

    @RequestMapping("get")
    public String get(@RequestParam("num") int num, @RequestParam("topic") String topic) {

        for (int i1 = 0; i1 < num; i1++) {
            Thread t = new Thread(() -> {
                Properties props = new Properties();
                props.put("bootstrap.servers", brokers);
                props.put("group.id", "test2");
                props.put("enable.auto.commit", "false");
                props.put("auto.commit.interval.ms", "1000");
                props.put("session.timeout.ms", "30000");
                props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singletonList(topic));
                final int minBatchSize = 200;
                List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
                while (true) {
                    System.out.println(String.format("线程id：【%s】运行中...", Thread.currentThread().getName()));

                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                    for (ConsumerRecord<String, String> record : records) {
                        buffer.add(record);
                        System.out.println(String.format("线程id：【%s】，分区：【%s】，接收到的消息：【%s】", Thread.currentThread().getName(), record.partition(), record.value()));
                    }
                    if (buffer.size() >= minBatchSize) {

                        consumer.commitSync();
                        buffer.clear();
                    }
                }
            }, String.format(String.format("线程-【%s】-【%s】", topic, i1)));

            t.start();
        }


        return "ok" + num;
    }

    public void init() {

        if (client != null) {
            return;
        }

        try {
            Properties admainProp = new Properties();
            admainProp.put("bootstrap.servers", brokers);
            client = AdminClient.create(admainProp);
            System.out.println("初始化KAFKA-Client成功!");
        } catch (Exception e) {
            System.out.println("初始化Client失败！");
        }
    }

    @RequestMapping("add-topic")
    public String addTopic(@RequestParam("topic") String topic, @RequestParam("partitions") int partitions, @RequestParam("replication") int replication) {

        init();

        try {
            NewTopic newTopic = new NewTopic(topic, partitions, (short) replication);
            Map<String, String> map = new HashMap<>(1);
            map.put("min.insync.replicas", "2");
            newTopic.configs(map);

            CreateTopicsOptions createTopicsOptions = new CreateTopicsOptions();
            createTopicsOptions.timeoutMs(Integer.valueOf("1000"));
            client.createTopics(Collections.singletonList(newTopic), createTopicsOptions);
            System.out.println("Topic创建成功！");
        } catch (Exception e) {
            System.out.println("创建失败：" + e.getMessage());
            return String.format("add-topic-error-%s-%s-%s", topic, partitions, replication);
        }

        return String.format("add-topic-ok-%s-%s-%s", topic, partitions, replication);
    }
}
