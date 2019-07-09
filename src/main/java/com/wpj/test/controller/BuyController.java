package com.wpj.test.controller;

import com.wpj.test.dao.BuyLogMapper;
import com.wpj.test.dao.entity.BuyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * @author wangpejian
 * @date 19-7-9 上午10:01
 */
@Slf4j
@RestController
public class BuyController {

    private final JedisCluster jedisCluster;

    private final BuyLogMapper buyLogMapper;

    public BuyController(JedisCluster jedisCluster, BuyLogMapper buyLogMapper) {
        this.jedisCluster = jedisCluster;
        this.buyLogMapper = buyLogMapper;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping("buy")
    public String buy() {
        String key = "commodity_limit";

        String script = "if tonumber(redis.call('get', KEYS[1])) > 0 then return redis.call('DECR', KEYS[1]) else return -1 end";
        Long result = (long) -1;

        try {
            result = (Long) jedisCluster.eval(script, Collections.singletonList(key), Collections.emptyList());
        } catch (Exception e) {
            log.error("redis操作异常：{}", e);
        }

        // 没货了
        if (result == -1) {
            return "sorry, commodity empty";
        }

        Date time = new Date();
        BuyLog logEntity = new BuyLog(UUID.randomUUID().toString().replaceAll("-", ""), result + 1, time);
        try {
            buyLogMapper.insert(logEntity);
            if (time.getTime() % 6 == 0) {
                throw new Exception("人为制造异常信息");
            }
        } catch (Exception e) {
            // 捕获异常，补偿商品数据
            jedisCluster.incr("commodity_limit");
            log.error("捕获人为异常：{}", e);

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "sorry, system error";
        }

        return "ok";
    }

}
