package com.ecommerce.dto;

import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole(Role.USER);
        testUser.setIsActive(true);
        testUser.setIsVerified(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testFromUser() {
        // Test that Lombok @Data annotation works correctly
        UserResponse response = UserResponse.fromUser(testUser);

        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getFirstName(), response.getFirstName());
        assertEquals(testUser.getLastName(), response.getLastName());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());
        assertEquals(testUser.getIsActive(), response.getIsActive());
        assertEquals(testUser.getIsVerified(), response.getIsVerified());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data generates getters, setters, equals, hashCode, toString
        UserResponse response1 = new UserResponse();
        response1.setId(1L);
        response1.setFirstName("John");
        response1.setLastName("Doe");
        response1.setEmail("john.doe@example.com");

        UserResponse response2 = new UserResponse();
        response2.setId(1L);
        response2.setFirstName("John");
        response2.setLastName("Doe");
        response2.setEmail("john.doe@example.com");

        // Test equals and hashCode
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test toString
        String toString = response1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john.doe@example.com"));
    }

    @Test
    void testAllArgsConstructor() {
        // Test that Lombok @AllArgsConstructor works
        LocalDateTime now = LocalDateTime.now();
        UserResponse response = new UserResponse(
            1L, "John", "Doe", "John Doe", "john.doe@example.com",
            null, null, null, Role.USER, true, true, now, now
        );

        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());
        assertTrue(response.getIsActive());
        assertTrue(response.getIsVerified());
    }

    @Test
    void testNoArgsConstructor() {
        // Test that Lombok @NoArgsConstructor works
        UserResponse response = new UserResponse();
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getFirstName());
        assertNull(response.getLastName());
    }
}
