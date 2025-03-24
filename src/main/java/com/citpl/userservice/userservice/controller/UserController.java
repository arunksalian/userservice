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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

	private final UserService userService;
	
	@Operation(
		summary = "Get user by ID",
		description = "Retrieves user information based on the user ID"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "User found",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = User.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "User not found",
			content = @Content
		)
	})
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(
		@Parameter(description = "User ID", required = true) @PathVariable String id,
		@Parameter(description = "Request ID for tracing", required = true) @RequestHeader("X-Request-Id") String requestId
	) {
		log.info("Getting user with id: {} (Request ID: {})", id, requestId);
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(
		summary = "Create new user",
		description = "Creates a new user with the provided information"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "User created successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = User.class)
			)
		)
	})
	@PostMapping
	public User createUser(
		@Parameter(description = "User details", required = true) @RequestBody User user,
		@Parameter(description = "Request ID for tracing", required = true) @RequestHeader("X-Request-Id") String requestId
	) {
		log.info("Creating new user (Request ID: {})", requestId);
		return userService.createUser(user);
	}

	@Operation(
		summary = "Update existing user",
		description = "Updates an existing user with the provided information"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "User updated successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = User.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "User not found",
			content = @Content
		)
	})
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(
		@Parameter(description = "User ID", required = true) @PathVariable String id,
		@Parameter(description = "User details", required = true) @RequestBody User user,
		@Parameter(description = "Request ID for tracing", required = true) @RequestHeader("X-Request-Id") String requestId
	) {
		log.info("Updating user with id: {} (Request ID: {})", id, requestId);
		user.setId(id);
		return userService.updateUser(user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(
		summary = "Delete user",
		description = "Deletes a user by ID"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "User deleted successfully"
		),
		@ApiResponse(
			responseCode = "404",
			description = "User not found",
			content = @Content
		)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(
		@Parameter(description = "User ID", required = true) @PathVariable String id,
		@Parameter(description = "Request ID for tracing", required = true) @RequestHeader("X-Request-Id") String requestId
	) {
		log.info("Deleting user with id: {} (Request ID: {})", id, requestId);
		return userService.deleteUser(id) 
				? ResponseEntity.ok().build()
				: ResponseEntity.notFound().build();
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
