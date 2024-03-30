package com.sinezx.shortlink.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CallbackInfo {
    private Integer id;
    private String createSn;
    private String code;
    private String content;
    private String callbackUrl;
    private Date createTime;
    private Date expireTime;
    private int expire;
    private String expireType;
}
