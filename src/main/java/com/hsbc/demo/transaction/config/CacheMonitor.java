package com.hsbc.demo.transaction.config;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CacheMonitor {
    
    private final CacheConfig cacheConfig;
    private final CacheManager cacheManager;
    
    public CacheMonitor(CacheConfig cacheConfig, CacheManager cacheManager) {
        this.cacheConfig = cacheConfig;
        this.cacheManager = cacheManager;
    }
    
    @Scheduled(fixedRate = 60000) // Execute every minute
    public void monitorCache() {
        cacheConfig.logCacheStats(cacheManager);
    }
}