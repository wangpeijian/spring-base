package com.wpj.test.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @auther wangpejian
 * @date 19-6-10 下午5:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private static final int OK = 200;
    private static final int ERROR = -1;

    private int code;

    private String msg;

    private T data;

    public Result ok(T data) {
        return new Result<>(OK, "", data);
    }

    public Result error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public Result error(String msg) {
        return new Result<>(ERROR, msg, null);
    }

    public Result error() {
        return new Result<>(ERROR, "", null);
    }
}
