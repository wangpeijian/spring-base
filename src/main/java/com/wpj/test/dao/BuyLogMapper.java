package com.wpj.test.dao;

import com.wpj.test.dao.entity.BuyLog;

public interface BuyLogMapper {
    int insert(BuyLog record);

    int insertSelective(BuyLog record);
}