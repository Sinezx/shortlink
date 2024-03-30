package com.sinezx.shortlink.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShortLinkVO {
    private String createSn;
    private String code;
    private String content;
    private String callbackUrl;
    private Date createTime;
    private Date expireTime;

    public ShortLinkVO(String code){
        this.code = code;
    }
}
