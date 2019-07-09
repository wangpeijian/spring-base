package com.wpj.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangpejian
 * @date 19-7-8 下午4:40
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisConfig {

    /**
     * 集群节点
     */
    private List<String> nodes=new ArrayList<>();

}
