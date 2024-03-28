package com.sinezx.shortlink.controller;

import com.sinezx.shortlink.mapper.mapstruct.CallbackInfoMapper;
import com.sinezx.shortlink.pojo.CallbackInfo;
import com.sinezx.shortlink.pojo.GenerateShortLink;
import com.sinezx.shortlink.pojo.GetShortLinkInfo;
import com.sinezx.shortlink.service.ShortLinkService;
import com.sinezx.shortlink.vo.ShortLinkVO;
import com.sinezx.shortlink.vo.base.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @RequestMapping("/generateshortlink")
    public Resp generateShortLink(@RequestBody GenerateShortLink generateShortLink){
        String code = shortLinkService.generateShortCode(generateShortLink.getContent(), generateShortLink.getCallbackUrl());
        return Resp.success(new ShortLinkVO(code));
    }

    @RequestMapping("/getshortlinkinfo")
    public Resp getShortLinkInfo(@RequestBody GetShortLinkInfo getShortLinkInfo){
        CallbackInfo callbackInfo = shortLinkService.getShortLinkInfo(getShortLinkInfo);
        if(ObjectUtils.isEmpty(callbackInfo)){
            return Resp.error("no data");
        }
        return Resp.success(CallbackInfoMapper.INSTANCE.callbackInfoToShortLinkVO(callbackInfo));
    }

}
