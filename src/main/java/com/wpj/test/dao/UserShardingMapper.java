package com.wpj.test.dao;

import com.github.pagehelper.Page;
import com.wpj.test.dao.entity.UserSharding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserShardingMapper {
    int insert(UserSharding record);

    int insertSelective(UserSharding record);

    List<UserSharding> getUserJoinList(@Param("id") long id);

    /**
     * select * from user where id > #{index} order by id
     */
    @Select("select * from user order by id")
    Page<UserSharding> getUserList(@Param("index") long index);

    @Select("select * from user where id = #{id}")
    UserSharding findUser(@Param("id") long id);
}