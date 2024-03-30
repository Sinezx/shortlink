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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @RequestMapping("/generateshortlink")
    public Resp generateShortLink(@RequestBody GenerateShortLink generateShortLink){
        String content = generateShortLink.getContent();
        String callbackUrl = generateShortLink.getCallbackUrl();
        int expire = generateShortLink.getExpire();
        String expireType = generateShortLink.getExpireType();
        if(ObjectUtils.isEmpty(callbackUrl)){
            return Resp.error("callbackUrl is null");
        }
        Pattern pattern = Pattern.compile("^http(s?)://");
        Matcher matcher = pattern.matcher(callbackUrl);
        if(matcher.lookingAt()) {
            String code = shortLinkService.generateShortCode(content, callbackUrl, expire, expireType);
            return Resp.success(new ShortLinkVO(code));
        }else{
            return Resp.error("callbackUrl invalid");
        }
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
