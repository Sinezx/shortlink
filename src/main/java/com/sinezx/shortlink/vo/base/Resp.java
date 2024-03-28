package com.sinezx.shortlink.vo.base;

public class Resp {

    private String code;
    private String msg;
    private Object data;

    public Resp(){}

    public Resp(String code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Resp success(Object data){
        return new Resp("0", null, data);
    }

    public static Resp error(String msg){
        return new Resp("1", msg, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
