package com.sinezx.shortlink.mapper;

import com.sinezx.shortlink.pojo.CallbackInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CallbackMapper {

    List<CallbackInfo> selectAll();

    void insertOne(CallbackInfo callbackInfo);

    CallbackInfo selectOneByCreateSn(String createSn);

    CallbackInfo selectOneByCode(String code);
}
