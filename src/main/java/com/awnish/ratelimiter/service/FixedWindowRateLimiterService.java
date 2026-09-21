package com.awnish.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class FixedWindowRateLimiterService {

    private final int capacity;
    private int currentToken;
    private final double refillTime;
    private double lastRefillTime;

   private ReentrantLock lock = new ReentrantLock();
    FixedWindowRateLimiterService(){
        this.capacity = 10;
        this.refillTime = 60.0;
        this.currentToken = 10;
        this.lastRefillTime=System.nanoTime();

    }

    public boolean allowRequest(){
        lock.lock();
        try{
            refillTime();
            if(currentToken>0){
                currentToken-=1;
                lastRefillTime = System.nanoTime();
                return true;
            }
            return false;
        }finally {
            lock.unlock();
        }
    }

    private void refillTime(){
        double now  = System.nanoTime();
        double timeDiff=(now-lastRefillTime)/1_000_000_000.0;
        System.out.println("refillTime: "+timeDiff);
        if(timeDiff>=refillTime){
            currentToken+=10;
            lastRefillTime=now;
        }
    }

}
