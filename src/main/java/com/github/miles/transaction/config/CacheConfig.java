package com.github.miles.transaction.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for caching
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Sets up the cache manager
     * For this simple application, we use a ConcurrentMapCacheManager
     * In a production environment, consider using Redis, Caffeine, or other distributed caching solutions
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("transactions");
    }
} 