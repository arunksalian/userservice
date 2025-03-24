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
import java.util.List;

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
        stats.put("writeCount", cache.getAdvancedCache().getStats().getStores());
        stats.put("removeCount", cache.getAdvancedCache().getStats().getRemoveHits());
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(summary = "Get cache health", description = "Retrieves health status of the users cache")
    public ResponseEntity<Map<String, Object>> getCacheHealth() {
        log.debug("Retrieving cache health status");
        Map<String, Object> health = new HashMap<>();
        
        try {
            Cache<String, Object> cache = cacheManager.getCache("users");
            if (cache != null) {
                health.put("status", cache.getStatus().toString());
                health.put("cacheName", "users");
                health.put("running", cache.getStatus().toString().equals("RUNNING"));
                health.put("cacheMode", cache.getCacheConfiguration().clustering().cacheMode().toString());
                health.put("clusterName", cacheManager.getClusterName());
                
                List<?> members = cacheManager.getMembers();
                health.put("numberOfNodes", members != null ? members.size() : 1);
                health.put("clusterType", members != null && !members.isEmpty() ? "CLUSTERED" : "LOCAL");
            } else {
                health.put("status", "NOT_FOUND");
                health.put("cacheName", "users");
                health.put("running", false);
            }
        } catch (Exception e) {
            log.error("Error getting cache health", e);
            health.put("status", "ERROR");
            health.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }
} 