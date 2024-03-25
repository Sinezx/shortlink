package com.sinezx.shortlink.util;

import jakarta.annotation.PostConstruct;
import org.checkerframework.checker.units.qual.N;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class MillsecondIncrTask {

    private final static int CAPACITY = 1000;

    private int threshold = 10000;

    private List<AtomicInteger> atomicIntegerList;

    @PostConstruct
    private void init(){
        atomicIntegerList = new ArrayList<>(CAPACITY);
        for(int i = 0; i < CAPACITY; i++){
            atomicIntegerList.add(new AtomicInteger(0));
        }
    }

    public int getLongValue(int ms){
        int abs = atomicIntegerList.get(ms).getAndIncrement() & Integer.MAX_VALUE;
        return abs % threshold;
    }

    public String getLongValueStr(int ms){
        return String.format("%04d", getLongValue(ms));
    }

}
