package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.UserResponse;
import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.Role;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Get current user's profile information")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "Profile retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve user profile: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve profile"));
        }
    }
    
    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Update current user's profile information")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        try {
            User currentUser = userService.getUserByEmail(authentication.getName());
            
            User userDetails = new User();
            userDetails.setFirstName(request.getFirstName());
            userDetails.setLastName(request.getLastName());
            userDetails.setPhoneNumber(request.getPhoneNumber());
            userDetails.setDateOfBirth(request.getDateOfBirth());
            userDetails.setAvatarUrl(request.getAvatarUrl());
            
            User updatedUser = userService.updateUser(currentUser.getId(), userDetails);
            UserResponse userResponse = UserResponse.fromUser(updatedUser);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "Profile updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update user profile: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update profile: " + e.getMessage()));
        }
    }
    
    @PutMapping("/change-password")
    @Operation(summary = "Change password", description = "Change current user's password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            userService.changePassword(user.getId(), request.getCurrentPassword(), request.getNewPassword());
            
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
            
        } catch (Exception e) {
            log.error("Failed to change password: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to change password: " + e.getMessage()));
        }
    }
    
    @PutMapping("/change-email")
    @Operation(summary = "Change email", description = "Change current user's email address")
    public ResponseEntity<ApiResponse<UserResponse>> changeEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            User updatedUser = userService.updateEmail(user.getId(), request.getNewEmail());
            UserResponse userResponse = UserResponse.fromUser(updatedUser);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "Email updated successfully. Please verify your new email address."));
            
        } catch (Exception e) {
            log.error("Failed to change email: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to change email: " + e.getMessage()));
        }
    }
    
    // Admin endpoints
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve paginated list of all users (Admin only)")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Filter by role") @RequestParam(required = false) Role role,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String search) {
        
        try {
            Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<User> users;
            
            if (search != null && !search.trim().isEmpty()) {
                users = userService.searchUsers(search, pageable);
            } else if (role != null) {
                users = userService.getUsersByRole(role, pageable);
            } else if (isActive != null) {
                users = userService.getActiveUsers(pageable);
            } else {
                users = userService.getAllUsers(pageable);
            }
            
            Page<UserResponse> userResponses = users.map(UserResponse::fromUser);
            
            return ResponseEntity.ok(ApiResponse.success(userResponses, "Users retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve users: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve users"));
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by ID (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user", description = "Update a user's information (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        try {
            User userDetails = new User();
            userDetails.setFirstName(request.getFirstName());
            userDetails.setLastName(request.getLastName());
            userDetails.setPhoneNumber(request.getPhoneNumber());
            userDetails.setDateOfBirth(request.getDateOfBirth());
            userDetails.setAvatarUrl(request.getAvatarUrl());
            
            User updatedUser = userService.updateUser(id, userDetails);
            UserResponse userResponse = UserResponse.fromUser(updatedUser);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update user: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate user", description = "Activate a user account (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(@PathVariable Long id) {
        try {
            User user = userService.activateUser(id);
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User activated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to activate user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to activate user"));
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(@PathVariable Long id) {
        try {
            User user = userService.deactivateUser(id);
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User deactivated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to deactivate user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to deactivate user"));
        }
    }
    
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verify user", description = "Manually verify a user account (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> verifyUser(@PathVariable Long id) {
        try {
            User user = userService.verifyUser(id);
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User verified successfully"));
            
        } catch (Exception e) {
            log.error("Failed to verify user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to verify user"));
        }
    }
    
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role", description = "Update a user's role (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, Role> request) {
        
        try {
            Role newRole = request.get("role");
            if (newRole == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Role is required"));
            }
            
            User user = userService.updateRole(id, newRole);
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User role updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update user role for {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update user role"));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Delete a user account (Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
            
        } catch (Exception e) {
            log.error("Failed to delete user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete user: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user statistics", description = "Get user statistics for admin dashboard (Admin only)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStats() {
        try {
            Map<String, Object> stats = Map.of(
                    "totalUsers", userService.getTotalUserCount(),
                    "activeUsers", userService.getActiveUserCount(),
                    "verifiedUsers", userService.getVerifiedUserCount(),
                    "adminUsers", userService.getUserCountByRole(Role.ADMIN),
                    "regularUsers", userService.getUserCountByRole(Role.USER),
                    "recentUsers", userService.getRecentUsers(10).size()
            );
            
            return ResponseEntity.ok(ApiResponse.success(stats, "User statistics retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve user statistics: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve user statistics"));
        }
    }
    
    // Request DTOs
    public static class UpdateProfileRequest {
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        private String lastName;
        
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private String avatarUrl;
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }
    
    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String newPassword;
        
        // Getters and setters
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
    
    public static class ChangeEmailRequest {
        @NotBlank(message = "New email is required")
        @Email(message = "Please provide a valid email address")
        private String newEmail;
        
        public String getNewEmail() { return newEmail; }
        public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
    }
    
    public static class UpdateUserRequest {
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        private String lastName;
        
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private String avatarUrl;
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }
}
