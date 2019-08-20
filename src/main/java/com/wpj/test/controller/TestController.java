package com.wpj.test.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpj.test.dao.UserMapper;
import com.wpj.test.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * @author wpj
 */
@RestController
public class TestController {

    private final UserMapper userMapper;

    private final JedisCluster jedisCluster;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public TestController(UserMapper userMapper, JedisCluster jedisCluster) {
        this.userMapper = userMapper;
        this.jedisCluster = jedisCluster;
    }

    @Transactional
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
        return jedisCluster.eval(script, Collections.singletonList(key), Collections.singletonList(value));
    }

    @GetMapping("test-redis-get")
    public String testRedisGet(@RequestParam("key") String key) {
        return jedisCluster.get(key);
    }

    /**
     * 浏览器指纹 + header key防爬虫
     *
     * @return
     */
    @RequestMapping("post")
    public String post() {
        String req = request.getQueryString();
        System.out.println(req);

        String res = "";

        System.out.println("ContextPath()===========================" + request.getRequestURI());

        try {
            String body = getBodyString(request);
            String str = request.getRequestURL() + "?" + req + body;
            System.out.println(str);
            String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
            System.out.println(md5);

            res = request.getHeader(md5);

            System.out.println("取到的指纹是：" + res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "".equals(res) ? "fail" : "ok";
    }

    public static String getBodyString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(
                    new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            char[] bodyCharBuffer = new char[1024];
            int len = 0;
            while ((len = reader.read(bodyCharBuffer)) != -1) {
                sb.append(new String(bodyCharBuffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
