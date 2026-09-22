package com.awnish.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SlidingWindowRateLimiterService {
    private final int capacity=5;
    private final double windowSize=60.0;
    private int currentToken=5;
    private double timestamp;

    private ReentrantLock lock=new ReentrantLock();
    private Queue<Long> queue= new LinkedList<Long>();
    SlidingWindowRateLimiterService(){
        this.timestamp=System.nanoTime();
    }

    public boolean allowRequest(){
        lock.lock();

        try{
            if(refine()){
                return true;
            }

            return false;
        }finally {
            lock.unlock();
        }

    }

    private boolean refine() {

        long now = System.nanoTime();

        long windowNanos = 60L * 1_000_000_000L;
        System.out.println("windowNanos: "+windowNanos);

        long cutoff = now - windowNanos;
        System.out.println("cutoff: "+cutoff);
        // Remove expired requests
        while (!queue.isEmpty() && queue.peek() <= cutoff) {
            System.out.println("queue: "+queue);
            queue.poll();
        }

        // Window already contains maximum requests
        if (queue.size() >= capacity) {
            System.out.println("inside capacity");
            return false;
        }

        // Current request is allowed
        queue.add(now);

        return true;
    }
}
