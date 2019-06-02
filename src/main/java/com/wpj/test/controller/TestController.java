package com.wpj.test.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpj.test.dao.UserMapper;
import com.wpj.test.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("test")
    public PageInfo test() {

        User user = new User();
        user.setId("123" + new Date().getTime());
        user.setName("test");
        userMapper.insertSelective(user);

        System.out.println(userMapper.selectById("7XDAH5WYeDtXCsWxg7o1eLdZpcwnOXZ7"));

        PageHelper.startPage(1, 2);
        Page<User> userList = userMapper.getUserList();
        return new PageInfo<>(userList);
    }

}
