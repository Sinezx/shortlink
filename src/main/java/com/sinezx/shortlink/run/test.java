package com.sinezx.shortlink.run;

import com.sinezx.shortlink.mapper.CallbackMapper;
import com.sinezx.shortlink.pojo.CallbackInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class test {

    @Autowired
    private CallbackMapper callbackMapper;

    @GetMapping("/")
    public List<CallbackInfo> test(){
        return callbackMapper.selectAll();
    }
}
