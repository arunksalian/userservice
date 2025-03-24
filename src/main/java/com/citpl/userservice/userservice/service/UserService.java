package com.citpl.userservice.userservice.service;

import com.citpl.userservice.userservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    // Simulating a database with a HashMap
    private final Map<String, User> userDb = new HashMap<>();

    @Cacheable(cacheNames = "users", key = "#id")
    public Optional<User> getUserById(String id) {
        log.info("Fetching user from database with id: {}", id);
        // Simulate database access delay
        simulateDelay();
        return Optional.ofNullable(userDb.get(id));
    }

    @CachePut(cacheNames = "users", key = "#user.id")
    public User createUser(User user) {
        log.info("Creating new user with id: {}", user.getId());
        userDb.put(user.getId(), user);
        return user;
    }

    @CachePut(cacheNames = "users", key = "#user.id")
    public Optional<User> updateUser(User user) {
        log.info("Updating user with id: {}", user.getId());
        if (userDb.containsKey(user.getId())) {
            userDb.put(user.getId(), user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @CacheEvict(cacheNames = "users", key = "#id")
    public boolean deleteUser(String id) {
        log.info("Deleting user with id: {}", id);
        return userDb.remove(id) != null;
    }

    @CacheEvict(cacheNames = "users", allEntries = true)
    public void clearCache() {
        log.info("Clearing all entries from cache");
    }

    private void simulateDelay() {
        try {
            // Simulate database access delay of 1 second
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 