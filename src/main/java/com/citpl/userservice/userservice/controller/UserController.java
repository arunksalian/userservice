package com.citpl.userservice.userservice.controller;

import com.citpl.userservice.userservice.model.User;
import com.citpl.userservice.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

	private final UserService userService;
	
	@GetMapping
	@Operation(summary = "Get all users", description = "Retrieves a list of all users")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved users")
	public ResponseEntity<List<User>> getAllUsers() {
		log.debug("Received request to get all users");
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<User> getUserById(
		@Parameter(description = "User ID", required = true)
		@PathVariable String id) {
		log.debug("Received request to get user with ID: {}", id);
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Create user", description = "Creates a new user")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "User created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	public ResponseEntity<User> createUser(
		@Parameter(description = "User details", required = true)
		@Valid @RequestBody User user) {
		log.debug("Received request to create user: {}", user);
		User createdUser = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update user", description = "Updates an existing user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User updated successfully"),
		@ApiResponse(responseCode = "404", description = "User not found"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	public ResponseEntity<User> updateUser(
		@Parameter(description = "User ID", required = true)
		@PathVariable String id,
		@Parameter(description = "Updated user details", required = true)
		@Valid @RequestBody User user) {
		log.debug("Received request to update user with ID: {} with data: {}", id, user);
		return userService.updateUser(id, user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete user", description = "Deletes a user by their ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "User deleted successfully"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<Void> deleteUser(
		@Parameter(description = "User ID", required = true)
		@PathVariable String id) {
		log.debug("Received request to delete user with ID: {}", id);
		if (userService.deleteUser(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Operation(
		summary = "Clear cache",
		description = "Clears all entries from the user cache"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Cache cleared successfully"
		)
	})
	@PostMapping("/cache/clear")
	public ResponseEntity<Void> clearCache(
		@Parameter(description = "Request ID for tracing", required = true) @RequestHeader("X-Request-Id") String requestId
	) {
		log.info("Clearing cache (Request ID: {})", requestId);
		userService.clearCache();
		return ResponseEntity.ok().build();
	}
}
