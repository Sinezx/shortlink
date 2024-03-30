package com.sinezx.shortlink.service;

import com.sinezx.shortlink.mapper.CallbackMapper;
import com.sinezx.shortlink.pojo.CallbackInfo;
import com.sinezx.shortlink.pojo.GetShortLinkInfo;
import com.sinezx.shortlink.util.ExpireTypeSelector;
import com.sinezx.shortlink.util.HashUtil;
import com.sinezx.shortlink.util.SerialNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class ShortLinkService {
    @Autowired
    private SerialNumberUtil serialNumberUtil;

    @Autowired
    private HashUtil hashUtil;

    @Autowired
    private CallbackMapper callbackMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String generateShortCode(String content, String callbackUrl, int expire, String expireType){
        //Legitimate expire operation
        ExpireTypeSelector expireTypeSelector = new ExpireTypeSelector(expire, expireType);
        return generateShortCodeDefault(content, callbackUrl, expireTypeSelector.getExpire(), expireTypeSelector.getExpireType());
    }

    public String generateShortCodeDefault(String content, String callbackUrl, int expire, String expireType){
        return transactionTemplate.execute(new TransactionCallback<String>(){

            @Override
            public String doInTransaction(TransactionStatus status) {
                try {
                    String createSn = serialNumberUtil.concurrencyGenerateSerialNumber();
                    String code = hashUtil.hash(createSn);
                    CallbackInfo callbackInfo = new CallbackInfo();
                    callbackInfo.setCode(code);
                    callbackInfo.setCreateSn(createSn);
                    callbackInfo.setContent(content);
                    callbackInfo.setCallbackUrl(callbackUrl);
                    callbackInfo.setExpire(expire);
                    callbackInfo.setExpireType(expireType);
                    callbackMapper.insertOne(callbackInfo);
                    return code;
                }catch (Exception e){
                    status.setRollbackOnly();
                    log.error(e.getMessage());
                    return null;
                }
            }
        });

    }

    public CallbackInfo getShortLinkInfo(GetShortLinkInfo getShortLinkInfo) {
        return callbackMapper.selectOneByCreateSn(getShortLinkInfo.getCreateSn());
    }

    public String getCallbackUrl(String code) {
        CallbackInfo callbackInfo = callbackMapper.selectOneByCode(code);
        String redirectUrl = "";
        if(!ObjectUtils.isEmpty(callbackInfo)){
            redirectUrl = callbackInfo.getCallbackUrl() + "?sn=" + callbackInfo.getCreateSn();
        }
        return redirectUrl;

    }
}
