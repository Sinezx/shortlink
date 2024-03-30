package com.sinezx.shortlink.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
