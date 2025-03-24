package com.citpl.userservice.userservice.service;

import com.citpl.userservice.userservice.model.User;
import com.citpl.userservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(key = "#id")
    public Optional<User> getUserById(String id) {
        log.debug("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    @CachePut(key = "#user.id")
    @Transactional
    public User createUser(User user) {
        log.debug("Creating new user: {}", user);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    @CachePut(key = "#id")
    @Transactional
    public Optional<User> updateUser(String id, User user) {
        log.debug("Updating user with ID: {} with data: {}", id, user);
        return userRepository.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    return userRepository.save(user);
                });
    }

    @CacheEvict(key = "#id")
    @Transactional
    public boolean deleteUser(String id) {
        log.debug("Deleting user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Cacheable(cacheNames = "users")
    public Page<User> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
        log.debug("Clearing user cache");
    }
} 