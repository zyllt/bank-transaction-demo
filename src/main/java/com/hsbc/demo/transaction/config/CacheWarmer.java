/**
 * @author miles.zeng
 * @since  
 */
package com.hsbc.demo.transaction.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class CacheWarmer implements ApplicationListener<ApplicationReadyEvent> {
    
    private final CacheConfig cacheConfig;
    
    
    @Override
    public void onApplicationEvent(@SuppressWarnings("null") @Nonnull ApplicationReadyEvent event) {
        cacheConfig.warmUpCache();
    }
} 
