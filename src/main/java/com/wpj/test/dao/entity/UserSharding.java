package com.wpj.test.dao.entity;

import lombok.Data;

@Data
public class UserSharding {
    private Long id;

    private String name;

    private Doc doc;

    public UserSharding(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserSharding() {
        super();
    }

}