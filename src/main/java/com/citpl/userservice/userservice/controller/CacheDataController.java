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
@RequestMapping("/monitor/cache/data")
@RequiredArgsConstructor
@Tag(name = "Cache Data Monitoring", description = "APIs for monitoring cache data")
public class CacheDataController {

    private final EmbeddedCacheManager cacheManager;

    @GetMapping
    @Operation(summary = "Get cache data", description = "Retrieves all entries from the users cache")
    public ResponseEntity<Map<String, Object>> getCacheData() {
        log.debug("Retrieving cache data");
        Map<String, Object> data = new HashMap<>();
        
        try {
            Cache<String, Object> cache = cacheManager.getCache("users");
            if (cache != null) {
                Map<String, Object> entries = new HashMap<>();
                cache.entrySet().forEach(entry -> entries.put(entry.getKey().toString(), entry.getValue()));
                
                data.put("entries", entries);
                data.put("size", cache.size());
            } else {
                data.put("error", "Cache not found");
            }
        } catch (Exception e) {
            log.error("Error retrieving cache data", e);
            data.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(data);
    }
} 