package com.wpj.test.dao;

import com.wpj.test.dao.entity.Doc;

public interface DocMapper {
    int insert(Doc record);

    int insertSelective(Doc record);
}