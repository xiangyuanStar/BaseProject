package com.baseproject.bean;

/**
 * 通用 bean，用于获取 code、message
 *
 * @author xiaoyuan.
 * @date 2019/3/14.
 */
public class GeneralBean {
    private int code;
    private String message;
    private Object t;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return t;
    }

    public void setData(Object t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "GeneralBean{" +
                "code=" + code +
                ", message='" + message +
                '}';
    }
}