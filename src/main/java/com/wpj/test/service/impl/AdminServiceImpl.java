package com.wpj.test.service.impl;

import com.wpj.test.dao.OperateAdminMapper;
import com.wpj.test.dao.entity.OperateAdmin;
import com.wpj.test.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangpejian
 * @date 19-6-11 下午6:07
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    OperateAdminMapper operateAdminMapper;

    @Override
    public OperateAdmin login(OperateAdmin operateAdmin) {

        OperateAdmin user = operateAdminMapper.findByUsernameAndPassword(operateAdmin.getUsername(), operateAdmin.getPassword());

        if(user == null){
            return null;
        }

        return user;
    }
}
