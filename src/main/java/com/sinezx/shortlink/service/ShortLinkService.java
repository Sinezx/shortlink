package com.sinezx.shortlink.service;

import com.sinezx.shortlink.mapper.CallbackMapper;
import com.sinezx.shortlink.pojo.CallbackInfo;
import com.sinezx.shortlink.pojo.GetShortLinkInfo;
import com.sinezx.shortlink.util.HashUtil;
import com.sinezx.shortlink.util.SerialNumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;

@Service
public class ShortLinkService {

    private static final Logger log = LoggerFactory.getLogger(ShortLinkService.class);

    @Autowired
    private SerialNumberUtil serialNumberUtil;

    @Autowired
    private HashUtil hashUtil;

    @Autowired
    private CallbackMapper callbackMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String generateShortCode(String content, String callbackUrl){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        return generateShortCode(content, callbackUrl, calendar.getTime());
    }

    public String generateShortCode(String content, String callbackUrl, Date date){
        return transactionTemplate.execute(new TransactionCallback<String>(){

            @Override
            public String doInTransaction(TransactionStatus status) {
                try {
                    String createSn = serialNumberUtil.concurrencyGenerateSerialNumber();
                    String code = hashUtil.hash(createSn);
                    CallbackInfo callbackInfo = new CallbackInfo();
                    callbackInfo.setCode(code);
                    callbackInfo.setCreateSn(createSn);
                    callbackInfo.setExpireTime(date);
                    callbackInfo.setContent(content);
                    callbackInfo.setCallbackUrl(callbackUrl);
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
