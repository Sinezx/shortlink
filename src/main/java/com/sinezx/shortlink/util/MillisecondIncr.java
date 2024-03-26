package com.sinezx.shortlink.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MillisecondIncr {

    private final static int CAPACITY = 1000;

    @Value("${ms.incr.threshold:10000}")
    private int threshold;
    @Value("${ms.incr.strFormat:%04d}")
    private String strFormat;

    private AtomicInteger[] atomicIntegerMap;

    @PostConstruct
    private void init(){
        atomicIntegerMap = new AtomicInteger[CAPACITY];
        for(int i = 0; i < CAPACITY; i++){
            atomicIntegerMap[i] = new AtomicInteger(0);
        }
    }

    public int getAndIncrValue(String ms){
        assert ms.length() == 3;
        return getAndIncrValue(Integer.parseInt(ms));
    }

    private int getAndIncrValue(int ms){
        assert 0 <= ms && ms < CAPACITY;
        int abs = atomicIntegerMap[ms].getAndIncrement() & Integer.MAX_VALUE;
        return abs % threshold;
    }

    public String getAndIncrValueStr(String ms){
        return String.format(strFormat, getAndIncrValue(ms));
    }

}
