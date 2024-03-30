package com.sinezx.shortlink.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateShortLink {
    private String content;

    private String callbackUrl;

    private int expire;

    private String expireType;
}
