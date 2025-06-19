package com.ecommerce.repository;

import com.ecommerce.entity.Address;
import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.AddressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // User-based queries
    List<Address> findByUser(User user);
    
    List<Address> findByUserId(Long userId);
    
    Page<Address> findByUserId(Long userId, Pageable pageable);
    
    // Default address queries
    Optional<Address> findByUserAndIsDefaultTrue(User user);
    
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
    
    List<Address> findByUserAndIsDefaultFalse(User user);
    
    List<Address> findByUserIdAndIsDefaultFalse(Long userId);
    
    // Type-based queries
    List<Address> findByUserAndType(User user, AddressType type);
    
    List<Address> findByUserIdAndType(Long userId, AddressType type);
    
    Optional<Address> findByUserAndTypeAndIsDefaultTrue(User user, AddressType type);
    
    Optional<Address> findByUserIdAndTypeAndIsDefaultTrue(Long userId, AddressType type);
    
    // Location-based queries
    List<Address> findByCity(String city);
    
    List<Address> findByState(String state);
    
    List<Address> findByCountry(String country);
    
    List<Address> findByCityAndState(String city, String state);
    
    List<Address> findByStateAndCountry(String state, String country);
    
    List<Address> findByCityAndStateAndCountry(String city, String state, String country);
    
    // ZIP code queries
    List<Address> findByZipCode(String zipCode);
    
    List<Address> findByZipCodeStartingWith(String zipCodePrefix);
    
    // Search queries
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND " +
           "(LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.street) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.state) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Address> searchUserAddresses(@Param("userId") Long userId, @Param("keyword") String keyword);
    
    // Count queries
    long countByUser(User user);
    
    long countByUserId(Long userId);
    
    long countByUserAndType(User user, AddressType type);
    
    long countByUserIdAndType(Long userId, AddressType type);
    
    // Existence checks
    boolean existsByUserAndIsDefaultTrue(User user);
    
    boolean existsByUserIdAndIsDefaultTrue(Long userId);
    
    boolean existsByUserAndTypeAndIsDefaultTrue(User user, AddressType type);
    
    boolean existsByUserIdAndTypeAndIsDefaultTrue(Long userId, AddressType type);
    
    // Complex queries
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId ORDER BY " +
           "CASE WHEN a.isDefault = true THEN 0 ELSE 1 END, a.createdAt DESC")
    List<Address> findUserAddressesOrderedByDefault(@Param("userId") Long userId);
    
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.type = :type ORDER BY " +
           "CASE WHEN a.isDefault = true THEN 0 ELSE 1 END, a.createdAt DESC")
    List<Address> findUserAddressesByTypeOrderedByDefault(@Param("userId") Long userId, @Param("type") AddressType type);
    
    // Bulk operations
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.id != :excludeId")
    int unsetDefaultForUserExcept(@Param("userId") Long userId, @Param("excludeId") Long excludeId);
    
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.type = :type AND a.id != :excludeId")
    int unsetDefaultForUserAndTypeExcept(@Param("userId") Long userId, @Param("type") AddressType type, @Param("excludeId") Long excludeId);
    
    @Modifying
    @Query("DELETE FROM Address a WHERE a.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
