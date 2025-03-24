package com.citpl.userservice.userservice.config;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.EnableCaching;

@org.springframework.context.annotation.Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Configuration initializeCache(EmbeddedCacheManager cacheManager) {
        if (!cacheManager.cacheExists("users")) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder
                .statistics()
                .enable()
                .memory()
                .maxCount(1000);
            
            cacheManager.defineConfiguration("users", builder.build());
        }
        return cacheManager.getCacheConfiguration("users");
    }
} 