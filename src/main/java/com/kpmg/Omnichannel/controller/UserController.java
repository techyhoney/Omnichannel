package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.ApiResponse;
import com.kpmg.omnichannel.dto.UserRequest;
import com.kpmg.omnichannel.dto.UserResponse;
import com.kpmg.omnichannel.model.KycStatus;
import com.kpmg.omnichannel.model.UserStatus;
import com.kpmg.omnichannel.model.UserType;
import com.kpmg.omnichannel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/type/{userType}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByType(@PathVariable UserType userType) {
        List<UserResponse> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByStatus(@PathVariable UserStatus status) {
        List<UserResponse> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @PutMapping("/{userId}/status/{status}")
    @Operation(summary = "Update user account status", 
               description = "Update user account status to ACTIVE, INACTIVE, or LOCKED")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "New status") @PathVariable UserStatus status) {
        UserResponse user = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully to " + status, user));
    }

    @PutMapping("/{userId}/kyc-status/{kycStatus}")
    @Operation(summary = "Update user KYC status", 
               description = "Update user KYC verification status to PENDING, VERIFIED, or REJECTED")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserKycStatus(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "New KYC status") @PathVariable KycStatus kycStatus) {
        UserResponse user = userService.updateUserKycStatus(userId, kycStatus);
        return ResponseEntity.ok(ApiResponse.success("User KYC status updated successfully to " + kycStatus, user));
    }
}
