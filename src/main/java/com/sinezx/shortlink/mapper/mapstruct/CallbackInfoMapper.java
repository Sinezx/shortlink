package com.sinezx.shortlink.mapper.mapstruct;

import com.sinezx.shortlink.pojo.CallbackInfo;
import com.sinezx.shortlink.vo.ShortLinkVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CallbackInfoMapper {

    CallbackInfoMapper INSTANCE = Mappers.getMapper(CallbackInfoMapper.class);

    ShortLinkVO callbackInfoToShortLinkVO(CallbackInfo callbackInfo);
}
