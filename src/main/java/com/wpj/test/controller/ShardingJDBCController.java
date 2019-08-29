package com.wpj.test.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpj.test.dao.UserShardingMapper;
import com.wpj.test.dao.entity.UserSharding;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangpejian
 * @date 19-8-29 下午3:42
 */
@RestController
@RequestMapping("sharding")
public class ShardingJDBCController {

    private static int anInt = 0;
    private final UserShardingMapper userShardingMapper;
    private final ExecutorService pool = new ThreadPoolExecutor(10,
            10, 0,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(), r -> {
        Thread t = new Thread(r);
        t.setName(String.format("插入线程-【%s】", ++anInt));
        return t;
    });

    public ShardingJDBCController(UserShardingMapper userShardingMapper) {
        this.userShardingMapper = userShardingMapper;
    }

    @RequestMapping("add-user-one")
    public String addUserOne() {

        UserSharding user = new UserSharding();
        user.setName("SNOWFLAKE");
        userShardingMapper.insertSelective(user);

        return "ok";
    }

    @RequestMapping("add-user")
    public String addUser() {

        AtomicLong integer = new AtomicLong(1);

        for (long i = 0; i < 100; i++) {

            UserSharding user = new UserSharding();
            long id = integer.getAndAdd(1);
            user.setName(String.format("%s", id));
            userShardingMapper.insertSelective(user);

//            pool.execute(() -> {
//
//            });

        }

        return "ok";
    }

    @RequestMapping("get-user")
    public PageInfo getUser(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {

        long s = System.currentTimeMillis();

        PageHelper.startPage(pageNum, pageSize);
        Page<UserSharding> userList = userShardingMapper.getUserList(pageNum * pageSize);

        System.out.println((System.currentTimeMillis() - s) / 1000);

        return new PageInfo<>(userList);
    }

    @RequestMapping("find-user")
    public UserSharding findUser(@RequestParam("id") long id) {
        return userShardingMapper.findUser(id);
    }

    @RequestMapping("user-join")
    public List<UserSharding> userJoin(@RequestParam("id") long id) {
        return userShardingMapper.getUserJoinList(id);
    }
}
