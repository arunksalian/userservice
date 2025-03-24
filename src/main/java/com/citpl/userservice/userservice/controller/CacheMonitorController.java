package com.citpl.userservice.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/monitor/cache")
@RequiredArgsConstructor
@Tag(name = "Cache Monitoring", description = "APIs for monitoring Infinispan cache")
public class CacheMonitorController {

    private final EmbeddedCacheManager cacheManager;

    @GetMapping("/stats")
    @Operation(summary = "Get cache statistics", description = "Retrieves statistics for the users cache")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        log.debug("Retrieving cache statistics");
        Cache<String, Object> cache = cacheManager.getCache("users");
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("size", cache.size());
        stats.put("hitCount", cache.getAdvancedCache().getStats().getHits());
        stats.put("missCount", cache.getAdvancedCache().getStats().getMisses());
        stats.put("evictionCount", cache.getAdvancedCache().getStats().getEvictions());
        stats.put("averageReadTime", cache.getAdvancedCache().getStats().getAverageReadTime());
        stats.put("averageWriteTime", cache.getAdvancedCache().getStats().getAverageWriteTime());
        stats.put("averageRemoveTime", cache.getAdvancedCache().getStats().getAverageRemoveTime());
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(summary = "Get cache health", description = "Retrieves health status of the users cache")
    public ResponseEntity<Map<String, Object>> getCacheHealth() {
        log.debug("Retrieving cache health status");
        Cache<String, Object> cache = cacheManager.getCache("users");
        Map<String, Object> health = new HashMap<>();
        
        health.put("status", "HEALTHY");
        health.put("cacheName", "users");
        health.put("numberOfNodes", cacheManager.getMembers().size());
        health.put("status", cache.getStatus().toString());
        
        return ResponseEntity.ok(health);
    }
} 