package com.hsbc.demo.transaction.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache Configuration Class - Using Caffeine Cache Manager
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Configuration
@EnableCaching
@Slf4j
@RequiredArgsConstructor
public class CacheConfig {

    public static final String TRANSACTION_CACHE_NAME = "transaction";

    /**
     * Caffeine Cache Manager in JVM heap memory
     * High-performance local cache solution, suitable for high concurrency scenarios
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Set predefined cache names
        cacheManager.setCacheNames(Arrays.asList(TRANSACTION_CACHE_NAME));

        // Transaction cache configuration
        Caffeine<Object, Object> transactionCaffeine = Caffeine.newBuilder()
                // Maximum cache entries
                .maximumSize(300)
                // Expiration time after write
                .expireAfterWrite(Duration.ofMinutes(10))
                // Expiration time after access
                .expireAfterAccess(Duration.ofMinutes(5))
                // Record statistics for monitoring
                .recordStats();

        // Set default Caffeine configuration
        cacheManager.setCaffeine(transactionCaffeine);

        return cacheManager;
    }

    /**
     * Log cache statistics
     */
    public void logCacheStats(CacheManager cacheManager) {
        if (cacheManager instanceof CaffeineCacheManager) {
            CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;
                org.springframework.cache.Cache cache = caffeineCacheManager.getCache("transaction");
            if (cache instanceof CaffeineCache) {
                CaffeineCache caffeineCache = (CaffeineCache) cache;
                com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                log.info("Transaction cache stats: {}", nativeCache.stats());
            }
        }
    }

    /**
     * Cache warm-up method
     *
     */
    public void warmUpCache() {
        // TODO: Not implemented in demo
    }
}