package com.sinezx.shortlink.controller;

import com.sinezx.shortlink.pojo.GenerateShortLink;
import com.sinezx.shortlink.service.ShortLinkService;
import com.sinezx.shortlink.vo.ShortLinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @RequestMapping("/generateshortlink")
    public ShortLinkVO generateShortLink(@RequestBody GenerateShortLink generateShortLink){
        String code = shortLinkService.generateShortCode(generateShortLink.getContent(), generateShortLink.getCallbackUrl());
        return new ShortLinkVO(code);
    }

}
