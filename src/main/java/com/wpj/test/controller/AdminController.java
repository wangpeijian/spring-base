package com.wpj.test.controller;

import com.wpj.test.base.Result;
import com.wpj.test.dao.entity.OperateAdmin;
import com.wpj.test.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpejian
 * @date 19-6-11 下午6:05
 */
@RestController
@RequestMapping("operate/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("login")
    public Result login(@RequestBody OperateAdmin operateAdmin) {

        OperateAdmin user = adminService.login(operateAdmin);

        if (user == null) {
            return new Result<>().error("用户名密码错误！");
        }

        return new Result<>().ok(user);
    }

}
