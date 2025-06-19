package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.LoginResponse;
import com.ecommerce.dto.TokenResponse;
import com.ecommerce.dto.UserResponse;
import com.ecommerce.entity.User;
import com.ecommerce.security.JwtTokenProvider;
import com.ecommerce.service.UserService;
import com.ecommerce.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController {
    
    private final UserService userService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("User registration attempt for email: {}", request.getEmail());
            
            // Create user
            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
            User savedUser = userService.createUser(user);
            
            // Send welcome email
            emailService.sendWelcomeEmail(savedUser);
            
            // Generate verification token and send email
            String verificationToken = jwtTokenProvider.generateVerificationToken(savedUser.getEmail());
            emailService.sendEmailVerification(savedUser, verificationToken);
            
            UserResponse userResponse = UserResponse.fromUser(savedUser);
            
            log.info("User registered successfully with ID: {}", savedUser.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(userResponse, "User registered successfully. Please check your email for verification."));
            
        } catch (Exception e) {
            log.error("Registration failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Login attempt for email: {}", request.getEmail());
            
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get user details
            User user = userService.getUserByEmail(request.getEmail());
            
            if (!user.getIsActive()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Account is deactivated. Please contact support."));
            }
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            
            LoginResponse loginResponse = new LoginResponse(
                    token,
                    refreshToken,
                    "Bearer",
                    jwtTokenProvider.getExpirationTime(),
                    UserResponse.fromUser(user)
            );
            
            log.info("User logged in successfully: {}", user.getEmail());
            
            return ResponseEntity.ok(ApiResponse.success(loginResponse, "Login successful"));
            
        } catch (Exception e) {
            log.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email or password"));
        }
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token", description = "Generate new access token using refresh token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            log.info("Token refresh attempt");
            
            if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid refresh token"));
            }
            
            String email = jwtTokenProvider.getEmailFromToken(request.getRefreshToken());
            User user = userService.getUserByEmail(email);
            
            if (!user.getIsActive()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Account is deactivated"));
            }
            
            // Generate new tokens
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            
            String newToken = jwtTokenProvider.generateToken(authentication);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            
            TokenResponse tokenResponse = new TokenResponse(
                    newToken,
                    newRefreshToken,
                    "Bearer",
                    jwtTokenProvider.getExpirationTime()
            );
            
            log.info("Token refreshed successfully for user: {}", email);
            
            return ResponseEntity.ok(ApiResponse.success(tokenResponse, "Token refreshed successfully"));
            
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Token refresh failed"));
        }
    }
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Send password reset email")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            log.info("Password reset request for email: {}", request.getEmail());
            
            User user = userService.getUserByEmail(request.getEmail());
            
            // Generate reset token
            String resetToken = jwtTokenProvider.generatePasswordResetToken(user.getEmail());
            
            // Send reset email
            emailService.sendPasswordResetEmail(user, resetToken);
            
            log.info("Password reset email sent to: {}", request.getEmail());
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Password reset email sent successfully",
                    "Please check your email for password reset instructions"
            ));
            
        } catch (Exception e) {
            log.error("Password reset failed for email {}: {}", request.getEmail(), e.getMessage());
            // Don't reveal if email exists or not for security
            return ResponseEntity.ok(ApiResponse.success(
                    "Password reset email sent successfully",
                    "If the email exists, you will receive password reset instructions"
            ));
        }
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using reset token")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            log.info("Password reset attempt with token");
            
            if (!jwtTokenProvider.validateToken(request.getToken())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid or expired reset token"));
            }
            
            String email = jwtTokenProvider.getEmailFromToken(request.getToken());
            User user = userService.getUserByEmail(email);
            
            // Update password
            userService.changePassword(user.getId(), user.getPassword(), request.getNewPassword());
            
            // Send confirmation email
            emailService.sendPasswordChangedNotification(user);
            
            log.info("Password reset successfully for user: {}", email);
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Password reset successful",
                    "Your password has been reset successfully"
            ));
            
        } catch (Exception e) {
            log.error("Password reset failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password reset failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/verify-email")
    @Operation(summary = "Verify email", description = "Verify user email using verification token")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        try {
            log.info("Email verification attempt");
            
            if (!jwtTokenProvider.validateToken(request.getToken())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid or expired verification token"));
            }
            
            String email = jwtTokenProvider.getEmailFromToken(request.getToken());
            User user = userService.getUserByEmail(email);
            
            if (user.getIsVerified()) {
                return ResponseEntity.ok(ApiResponse.success(
                        "Email already verified",
                        "Your email is already verified"
                ));
            }
            
            // Verify user
            userService.verifyUser(user.getId());
            
            log.info("Email verified successfully for user: {}", email);
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Email verified successfully",
                    "Your email has been verified successfully"
            ));
            
        } catch (Exception e) {
            log.error("Email verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email verification failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate token")
    public ResponseEntity<ApiResponse<String>> logout() {
        try {
            // Clear security context
            SecurityContextHolder.clearContext();
            
            log.info("User logged out successfully");
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Logout successful",
                    "You have been logged out successfully"
            ));
            
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Logout failed"));
        }
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user details")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            
            UserResponse userResponse = UserResponse.fromUser(user);
            
            return ResponseEntity.ok(ApiResponse.success(userResponse, "User details retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to get current user: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get user details"));
        }
    }
    
    // Request/Response DTOs
    public static class RegisterRequest {
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        private String lastName;
        
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;
        
        @NotBlank(message = "Password is required")
        private String password;
        
        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class RefreshTokenRequest {
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
        
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
    
    public static class ForgotPasswordRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    public static class ResetPasswordRequest {
        @NotBlank(message = "Token is required")
        private String token;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String newPassword;
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
    
    public static class VerifyEmailRequest {
        @NotBlank(message = "Token is required")
        private String token;
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
