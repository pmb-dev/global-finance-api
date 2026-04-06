package com.github.pmbdev.global_finance_api.service;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Map to save a "bucket" for each IP
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        // Limit config: 10 request each minute
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(50).refillGreedy(50, Duration.ofMinutes(1)))
                .build();
    }
}