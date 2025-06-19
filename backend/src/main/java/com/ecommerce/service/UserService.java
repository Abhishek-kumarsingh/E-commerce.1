package com.ecommerce.service;

import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.Role;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    
    // Create operations
    @Transactional
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException("User already exists with email: " + user.getEmail());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setIsActive(true);
        user.setIsVerified(false);
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    @Transactional
    public User createAdminUser(User user) {
        log.info("Creating new admin user with email: {}", user.getEmail());
        
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException("User already exists with email: " + user.getEmail());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        user.setIsActive(true);
        user.setIsVerified(true);
        
        User savedUser = userRepository.save(user);
        log.info("Admin user created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    // Read operations
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
    
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    public Page<User> getActiveUsers(Pageable pageable) {
        return userRepository.findByIsActive(true, pageable);
    }
    
    public Page<User> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }
    
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword, pageable);
    }
    
    // Update operations
    @Transactional
    public User updateUser(Long id, User userDetails) {
        log.info("Updating user with ID: {}", id);
        
        User user = getUserById(id);
        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        user.setAvatarUrl(userDetails.getAvatarUrl());
        
        if (userDetails.getPreferences() != null) {
            user.setPreferences(userDetails.getPreferences());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }
    
    @Transactional
    public User changePassword(Long id, String currentPassword, String newPassword) {
        log.info("Changing password for user ID: {}", id);
        
        User user = getUserById(id);
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        
        log.info("Password changed successfully for user ID: {}", id);
        return updatedUser;
    }
    
    @Transactional
    public User updateEmail(Long id, String newEmail) {
        log.info("Updating email for user ID: {} to: {}", id, newEmail);
        
        if (userRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new IllegalArgumentException("Email already exists: " + newEmail);
        }
        
        User user = getUserById(id);
        user.setEmail(newEmail);
        user.setIsVerified(false); // Require re-verification
        
        User updatedUser = userRepository.save(user);
        log.info("Email updated successfully for user ID: {}", id);
        return updatedUser;
    }
    
    @Transactional
    public User verifyUser(Long id) {
        log.info("Verifying user with ID: {}", id);
        
        User user = getUserById(id);
        user.setIsVerified(true);
        
        User verifiedUser = userRepository.save(user);
        log.info("User verified successfully with ID: {}", id);
        return verifiedUser;
    }
    
    @Transactional
    public User activateUser(Long id) {
        log.info("Activating user with ID: {}", id);
        
        User user = getUserById(id);
        user.setIsActive(true);
        
        User activatedUser = userRepository.save(user);
        log.info("User activated successfully with ID: {}", id);
        return activatedUser;
    }
    
    @Transactional
    public User deactivateUser(Long id) {
        log.info("Deactivating user with ID: {}", id);
        
        User user = getUserById(id);
        user.setIsActive(false);
        
        User deactivatedUser = userRepository.save(user);
        log.info("User deactivated successfully with ID: {}", id);
        return deactivatedUser;
    }
    
    @Transactional
    public User updateRole(Long id, Role role) {
        log.info("Updating role for user ID: {} to: {}", id, role);
        
        User user = getUserById(id);
        user.setRole(role);
        
        User updatedUser = userRepository.save(user);
        log.info("Role updated successfully for user ID: {}", id);
        return updatedUser;
    }
    
    // Delete operations
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        User user = getUserById(id);
        userRepository.delete(user);
        
        log.info("User deleted successfully with ID: {}", id);
    }
    
    // Utility methods
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
    
    public long getTotalUserCount() {
        return userRepository.count();
    }
    
    public long getActiveUserCount() {
        return userRepository.countActiveUsers();
    }
    
    public long getVerifiedUserCount() {
        return userRepository.countVerifiedUsers();
    }
    
    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    public List<User> getRecentUsers(int limit) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return userRepository.findUsersCreatedAfter(oneWeekAgo);
    }
    
    public List<User> getActiveVerifiedUsersByRole(Role role) {
        return userRepository.findActiveVerifiedUsersByRole(role);
    }
    
    // Bulk operations
    @Transactional
    public void activateUsers(List<Long> userIds) {
        log.info("Activating {} users", userIds.size());
        userRepository.updateActiveStatusByIds(userIds, true);
        log.info("Users activated successfully");
    }
    
    @Transactional
    public void deactivateUsers(List<Long> userIds) {
        log.info("Deactivating {} users", userIds.size());
        userRepository.updateActiveStatusByIds(userIds, false);
        log.info("Users deactivated successfully");
    }
    
    @Transactional
    public void verifyUsers(List<Long> userIds) {
        log.info("Verifying {} users", userIds.size());
        userRepository.updateVerifiedStatusByIds(userIds, true);
        log.info("Users verified successfully");
    }
}
