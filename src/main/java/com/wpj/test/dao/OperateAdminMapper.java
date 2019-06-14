package com.wpj.test.dao;

import com.wpj.test.dao.entity.OperateAdmin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OperateAdminMapper {
    int insert(OperateAdmin record);

    int insertSelective(OperateAdmin record);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from operate_admin where username = #{username} and password = #{password}")
    OperateAdmin findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}