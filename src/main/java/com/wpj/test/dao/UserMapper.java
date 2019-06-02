package com.wpj.test.dao;

import com.github.pagehelper.Page;
import com.wpj.test.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    @Select("select * from c_user where id = #{id}")
    User selectById(@Param("id") String id);

    @Select("select * from c_user")
    Page<User> getUserList();
}