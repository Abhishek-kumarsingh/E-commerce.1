package com.ecommerce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify Lombok annotations are working correctly in VS Code
 */
@Slf4j
class LombokTest {

    @Test
    void testLombokAnnotations() {
        log.info("Testing Lombok annotations...");
        
        // Test @Data annotation
        TestData data1 = new TestData("John", 25);
        TestData data2 = new TestData("John", 25);
        
        // Test generated equals and hashCode
        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
        
        // Test generated getters
        assertEquals("John", data1.getName());
        assertEquals(25, data1.getAge());
        
        // Test generated setters
        data1.setName("Jane");
        data1.setAge(30);
        assertEquals("Jane", data1.getName());
        assertEquals(30, data1.getAge());
        
        // Test generated toString
        String toString = data1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Jane"));
        assertTrue(toString.contains("30"));
        
        // Test @NoArgsConstructor
        TestData emptyData = new TestData();
        assertNotNull(emptyData);
        assertNull(emptyData.getName());
        assertEquals(0, emptyData.getAge());
        
        log.info("All Lombok tests passed! ✅");
    }
    
    /**
     * Test class using Lombok annotations
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestData {
        private String name;
        private int age;
    }
}
