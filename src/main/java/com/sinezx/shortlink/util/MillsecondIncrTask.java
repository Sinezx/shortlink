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

    private List<Node> atomicIntegerList;

    private Node currentNode;

    @PostConstruct
    private void init(){
        atomicIntegerList = new ArrayList<>(CAPACITY);
        for(int i = 0; i < CAPACITY; i++){
            atomicIntegerList.add(new Node());
        }
        for(int i = 0; i < CAPACITY - 1; i++){
            atomicIntegerList.get(i).setNextNode(atomicIntegerList.get(i + 1));
        }
        atomicIntegerList.get(CAPACITY - 1).setNextNode(atomicIntegerList.get(0));
        currentNode = atomicIntegerList.get(0);
    }

    @Scheduled(fixedDelay = 1)
    public void resetLongAdder(){
        currentNode = currentNode.nextNode;
        currentNode.set(0);
    }

    public int getLongValue(int ms){
        return atomicIntegerList.get(ms).getAndIncrement();
    }

    public String getLongValueStr(int ms){
        return String.format("%03d", getLongValue(ms));
    }

    class Node extends AtomicInteger{
        private Node nextNode;

        public void setNextNode(Node next){
            nextNode = next;
        }

        public Node next(){
            return nextNode;
        }
    }
}
