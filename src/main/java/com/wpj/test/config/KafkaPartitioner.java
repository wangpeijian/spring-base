package com.wpj.test.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * @author wangpejian
 * @date 19-8-20 下午2:18
 */
public class KafkaPartitioner implements Partitioner {

    @Override
    public void configure(Map<String, ?> configs) {
        // TODO nothing
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 得到 topic 的 partitions 信息
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        // 模拟某客服
        Integer keyNum = new Integer((String) key);
        if (keyNum % 2 == 0) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public void close() {
        // TODO nothing
    }

}
