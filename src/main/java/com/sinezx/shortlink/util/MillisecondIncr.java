package com.sinezx.shortlink.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicIntegerArray;

@Component
public class MillisecondIncr {

    private final static int CAPACITY = 1000;

    @Value("${ms.incr.threshold:9999}")
    private int threshold;
    @Value("${ms.incr.strFormat:%04d}")
    private String strFormat;

    private AtomicIntegerArray atomicIntegerArray;

    @PostConstruct
    private void init(){
        atomicIntegerArray = new AtomicIntegerArray(threshold);
    }

    public int getAndIncrValue(String ms){
        assert ms.length() == 3;
        return getAndIncrValue(Integer.parseInt(ms));
    }

    private int getAndIncrValue(int ms){
        assert 0 <= ms && ms < CAPACITY;
        return atomicIntegerArray.getAndUpdate(ms, i -> i == threshold ? 0 : i + 1);
    }

    public String getAndIncrValueStr(String ms){
        return String.format(strFormat, getAndIncrValue(ms));
    }

}
