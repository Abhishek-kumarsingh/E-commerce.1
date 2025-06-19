package com.ecommerce.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableMongoAuditing
@EnableTransactionManagement
@EntityScan(basePackages = "com.ecommerce.entity")
@EnableJpaRepositories(
    basePackages = "com.ecommerce.repository",
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
        classes = {
            com.ecommerce.repository.ProductRepository.class,
            com.ecommerce.repository.CategoryRepository.class
        }
    )
)
@EnableMongoRepositories(
    basePackages = "com.ecommerce.repository",
    includeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
        classes = {
            com.ecommerce.repository.ProductRepository.class,
            com.ecommerce.repository.CategoryRepository.class
        }
    )
)
public class DatabaseConfig {
    // Configuration is handled through annotations and application properties
}
