package com.wpj.test.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BuyLog {
    private String id;

    private Long commodityIndex;

    private Date createTime;

    public BuyLog(String id, Long commodityIndex, Date createTime) {
        this.id = id;
        this.commodityIndex = commodityIndex;
        this.createTime = createTime;
    }

    public BuyLog() {
        super();
    }

}