package com.wpj.test.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpj.test.dao.UserMapper;
import com.wpj.test.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author wpj
 */
@RestController
public class TestController {

    private final UserMapper userMapper;

    private final JedisCluster jedisCluster;

    @Autowired
    public TestController(UserMapper userMapper, JedisCluster jedisCluster) {
        this.userMapper = userMapper;
        this.jedisCluster = jedisCluster;
    }


    @GetMapping("test")
    public PageInfo test() {

        User user = new User();
        user.setId("123" + System.currentTimeMillis());
        user.setName("test");
        userMapper.insertSelective(user);

        System.out.println(userMapper.selectById("7XDAH5WYeDtXCsWxg7o1eLdZpcwnOXZ7"));

        PageHelper.startPage(1, 2);
        Page<User> userList = userMapper.getUserList();
        return new PageInfo<>(userList);
    }

    @GetMapping("test-api")
    public User testAPI() {
        User user = new User();
        user.setId("123" + System.currentTimeMillis());
        user.setName("test");
        return user;
    }

    @GetMapping("test-redis-lock")
    public String testRedisLock(@RequestParam("key") String key, @RequestParam("value") String value) {
        SetParams params = new SetParams();
        params.ex(2);
        params.nx();
        return jedisCluster.set(key, value, params);
    }

    @GetMapping("test-redis-unlock")
    public Object testRedisUnlock(@RequestParam("key") String key, @RequestParam("value") String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return jedisCluster.eval(script,  Collections.singletonList(key),  Collections.singletonList(value));
    }

    @GetMapping("test-redis-get")
    public String testRedisGet(@RequestParam("key") String key) {
        return jedisCluster.get(key);
    }
}
