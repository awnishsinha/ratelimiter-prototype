package com.awnish.ratelimiter.controller;

import com.awnish.ratelimiter.service.FixedWindowRateLimiterService;
import com.awnish.ratelimiter.service.RateLimiterService;
import com.awnish.ratelimiter.service.SlidingWindowRateLimiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    private final RateLimiterService rateLimiter;
    private final FixedWindowRateLimiterService fixedWindowRateLimiter;
    private final SlidingWindowRateLimiterService slidingWindowRateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiter,  FixedWindowRateLimiterService fixedWindowRateLimiter,SlidingWindowRateLimiterService slidingWindowRateLimiterService) {
        this.rateLimiter = rateLimiter;
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
        this.slidingWindowRateLimiterService = slidingWindowRateLimiterService;
    }

    //using Token Bucket
    @GetMapping("/api/tokenbucket")
    public ResponseEntity<String> tokenBucket() {
        if(rateLimiter.allowRequest()){
            return ResponseEntity.ok("Hello World!");
        }else{
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }

    @GetMapping("/api/fixedwindow")
    public ResponseEntity<String> fixedWindow(){
        if(fixedWindowRateLimiter.allowRequest()){
            return ResponseEntity.ok("Hello World!");
        }else{
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }

    @GetMapping("/api/slidingwindow")

    public ResponseEntity<String> slidingWindow(){
        if(slidingWindowRateLimiterService.allowRequest()){
            return ResponseEntity.ok("Hello World!");
        }else{
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}
