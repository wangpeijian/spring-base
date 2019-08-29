package com.wpj.test.dao.entity;

import lombok.Data;

@Data
public class Doc {
    private Integer id;

    private Byte type;

    private Byte value;

    private String text;

    public Doc(Integer id, Byte type, Byte value, String text) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.text = text;
    }

    public Doc() {
        super();
    }


}