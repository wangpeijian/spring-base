package com.wpj.test.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author wangpejian
 * @date 19-8-29 下午3:30
 */
@Slf4j
public class ShardingConfig implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        for (String tableName : collection) {
            if (tableName.endsWith(preciseShardingValue.getValue() % 5 + "")) {
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
