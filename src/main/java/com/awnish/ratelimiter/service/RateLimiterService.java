package com.awnish.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class RateLimiterService {

    private double currentTokens;
    private final double refillRatePerSecond;
    private final int capacity;
    private double lastRefillTime;

    private ReentrantLock lock = new ReentrantLock();

    public RateLimiterService() {
        this.capacity = 10;
        this.currentTokens = capacity;
        this.refillRatePerSecond = 2.0;
        this.lastRefillTime = System.nanoTime();
    }

    public boolean allowRequest() {
        lock.lock();

        try{
            refill();
            if(currentTokens >= 1) {
                currentTokens -= 1;
                lastRefillTime = System.nanoTime();
                return true;
            }

            return false;
        }finally {
            lock.unlock();
        }

    }

    public void refill() {

            double secDifference = (System.nanoTime() - lastRefillTime)/1_000_000_000.0;
            double tokenToBeRefill= refillRatePerSecond * secDifference;
            currentTokens= Math.min(capacity, currentTokens+ tokenToBeRefill);
            if(currentTokens >0) {
                lastRefillTime = System.nanoTime();
            }

    }
}
