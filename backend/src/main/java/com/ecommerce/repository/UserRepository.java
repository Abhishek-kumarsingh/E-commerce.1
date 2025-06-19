package com.ecommerce.repository;

import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // Basic queries
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailIgnoreCase(String email);
    
    // Role-based queries
    List<User> findByRole(Role role);
    
    Page<User> findByRole(Role role, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);
    
    // Status-based queries
    List<User> findByIsActive(Boolean isActive);
    
    List<User> findByIsVerified(Boolean isVerified);
    
    List<User> findByIsActiveAndIsVerified(Boolean isActive, Boolean isVerified);
    
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);
    
    Page<User> findByIsVerified(Boolean isVerified, Pageable pageable);
    
    // Search queries
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.isActive = true AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchActiveUsers(@Param("keyword") String keyword, Pageable pageable);
    
    // Date-based queries
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT u FROM User u WHERE u.updatedAt >= :date")
    List<User> findUsersUpdatedAfter(@Param("date") LocalDateTime date);
    
    // Statistics queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    long countUsersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isVerified = true")
    long countVerifiedUsers();
    
    // Complex queries
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true AND u.isVerified = true")
    List<User> findActiveVerifiedUsersByRole(@Param("role") Role role);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(@Param("id") Long id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
    Optional<User> findByIdWithOrders(@Param("id") Long id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cartItems WHERE u.id = :id")
    Optional<User> findByIdWithCartItems(@Param("id") Long id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.wishlistItems WHERE u.id = :id")
    Optional<User> findByIdWithWishlistItems(@Param("id") Long id);
    
    // Bulk operations
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.id IN :ids")
    int updateActiveStatusByIds(@Param("ids") List<Long> ids, @Param("isActive") Boolean isActive);
    
    @Query("UPDATE User u SET u.isVerified = :isVerified WHERE u.id IN :ids")
    int updateVerifiedStatusByIds(@Param("ids") List<Long> ids, @Param("isVerified") Boolean isVerified);
}
