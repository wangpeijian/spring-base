package com.wpj.test.dao.entity;

import java.util.Date;

public class User {
    private String id;

    private String phone;

    private Date updatetime;

    private String name;

    private String password;

    public User(String id, String phone, Date updatetime, String name, String password) {
        this.id = id;
        this.phone = phone;
        this.updatetime = updatetime;
        this.name = name;
        this.password = password;
    }

    public User() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}