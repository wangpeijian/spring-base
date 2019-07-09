package com.wpj.test.bean;

import com.wpj.test.config.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangpejian
 * @date 19-7-8 下午4:46
 */
@Configuration
public class RedisClusterConfig {

    private final RedisConfig redisConfig;

    @Autowired
    public RedisClusterConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Bean
    public JedisCluster redisCluster() {

        Set<HostAndPort> nodes = new HashSet<>();
        for (String node : redisConfig.getNodes()) {
            String[] parts = StringUtils.split(node, ":");
            assert parts != null;
            Assert.state(parts.length == 2, "redis node shoule be defined as 'host:port', not '" + Arrays.toString(parts) + "'");
            nodes.add(new HostAndPort(parts[0], Integer.valueOf(parts[1])));
        }

        return new JedisCluster(nodes, 100, 1000, 2, "123456", new GenericObjectPoolConfig());
    }

}
