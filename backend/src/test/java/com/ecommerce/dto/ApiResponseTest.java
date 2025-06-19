package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testSuccessResponse() {
        String data = "Test data";
        String message = "Success message";
        
        ApiResponse<String> response = ApiResponse.success(data, message);
        
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testErrorResponse() {
        String errorMessage = "Error occurred";
        
        ApiResponse<String> response = ApiResponse.error(errorMessage);
        
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorMessage, response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data generates getters, setters, equals, hashCode, toString
        ApiResponse<String> response1 = new ApiResponse<>();
        response1.setSuccess(true);
        response1.setMessage("Test message");
        response1.setData("Test data");

        ApiResponse<String> response2 = new ApiResponse<>();
        response2.setSuccess(true);
        response2.setMessage("Test message");
        response2.setData("Test data");

        // Test getters
        assertTrue(response1.isSuccess());
        assertEquals("Test message", response1.getMessage());
        assertEquals("Test data", response1.getData());

        // Test toString
        String toString = response1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains("Test data"));
    }

    @Test
    void testAllArgsConstructor() {
        // Test that Lombok @AllArgsConstructor works
        ApiResponse<String> response = new ApiResponse<>(
            true, "Success", "Test data", null, null
        );

        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals("Test data", response.getData());
        assertNull(response.getError());
    }

    @Test
    void testNoArgsConstructor() {
        // Test that Lombok @NoArgsConstructor works
        ApiResponse<String> response = new ApiResponse<>();
        assertNotNull(response);
        assertFalse(response.isSuccess()); // boolean defaults to false
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertNull(response.getError());
    }
}
