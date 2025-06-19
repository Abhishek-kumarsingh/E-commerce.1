package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve paginated list of products with optional filters")
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Category filter") @RequestParam(required = false) String category,
            @Parameter(description = "Brand filter") @RequestParam(required = false) String brand,
            @Parameter(description = "Minimum price") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String search,
            @Parameter(description = "Show only active products") @RequestParam(defaultValue = "true") boolean activeOnly) {
        
        try {
            Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Product> products;
            
            if (search != null && !search.trim().isEmpty()) {
                products = productService.searchProducts(search, pageable);
            } else if (category != null && !category.trim().isEmpty()) {
                products = productService.getProductsByCategory(category, pageable);
            } else if (brand != null && !brand.trim().isEmpty()) {
                products = productService.getProductsByBrand(brand, pageable);
            } else if (minPrice != null && maxPrice != null) {
                products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
            } else if (activeOnly) {
                products = productService.getActiveProducts(pageable);
            } else {
                products = productService.getAllProducts(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve products: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id);
            
            // Increment view count
            productService.incrementViewCount(id);
            
            return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Product not found"));
        }
    }
    
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieve a specific product by its SKU")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(@PathVariable String sku) {
        try {
            Product product = productService.getProductBySku(sku);
            return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve product with SKU {}: {}", sku, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Product not found"));
        }
    }
    
    @GetMapping("/featured")
    @Operation(summary = "Get featured products", description = "Retrieve featured products")
    public ResponseEntity<ApiResponse<Page<Product>>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.getFeaturedProducts(pageable);
            
            return ResponseEntity.ok(ApiResponse.success(products, "Featured products retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve featured products: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve featured products"));
        }
    }
    
    @GetMapping("/popular")
    @Operation(summary = "Get popular products", description = "Retrieve most viewed or best selling products")
    public ResponseEntity<ApiResponse<List<Product>>> getPopularProducts(
            @RequestParam(defaultValue = "viewed") String type,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Product> products;
            
            switch (type.toLowerCase()) {
                case "bestselling":
                    products = productService.getBestSellingProducts(pageable);
                    break;
                case "toprated":
                    products = productService.getTopRatedProducts(pageable);
                    break;
                case "recent":
                    products = productService.getRecentProducts(pageable);
                    break;
                default:
                    products = productService.getMostViewedProducts(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(products, "Popular products retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve popular products: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve popular products"));
        }
    }
    
    @GetMapping("/{id}/related")
    @Operation(summary = "Get related products", description = "Retrieve products related to a specific product")
    public ResponseEntity<ApiResponse<List<Product>>> getRelatedProducts(
            @PathVariable String id,
            @RequestParam(defaultValue = "8") int limit) {
        
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Product> products = productService.getRelatedProducts(id, pageable);
            
            return ResponseEntity.ok(ApiResponse.success(products, "Related products retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve related products for {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve related products"));
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product", description = "Create a new product (Admin only)")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.createProduct(product);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(savedProduct, "Product created successfully"));
            
        } catch (Exception e) {
            log.error("Failed to create product: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create product: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody Product productDetails) {
        
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update product: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product stock", description = "Update product stock quantity (Admin only)")
    public ResponseEntity<ApiResponse<Product>> updateStock(
            @PathVariable String id,
            @RequestBody Map<String, Integer> request) {
        
        try {
            Integer newStock = request.get("stock");
            if (newStock == null || newStock < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid stock quantity"));
            }
            
            Product updatedProduct = productService.updateStock(id, newStock);
            
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Stock updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update stock for product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update stock: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product price", description = "Update product price (Admin only)")
    public ResponseEntity<ApiResponse<Product>> updatePrice(
            @PathVariable String id,
            @RequestBody Map<String, BigDecimal> request) {
        
        try {
            BigDecimal newPrice = request.get("price");
            if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid price"));
            }
            
            Product updatedProduct = productService.updatePrice(id, newPrice);
            
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Price updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update price for product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update price: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate product", description = "Activate a product (Admin only)")
    public ResponseEntity<ApiResponse<Product>> activateProduct(@PathVariable String id) {
        try {
            Product product = productService.activateProduct(id);
            return ResponseEntity.ok(ApiResponse.success(product, "Product activated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to activate product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to activate product"));
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate product", description = "Deactivate a product (Admin only)")
    public ResponseEntity<ApiResponse<Product>> deactivateProduct(@PathVariable String id) {
        try {
            Product product = productService.deactivateProduct(id);
            return ResponseEntity.ok(ApiResponse.success(product, "Product deactivated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to deactivate product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to deactivate product"));
        }
    }
    
    @PatchMapping("/{id}/feature")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Feature product", description = "Mark product as featured (Admin only)")
    public ResponseEntity<ApiResponse<Product>> featureProduct(@PathVariable String id) {
        try {
            Product product = productService.featureProduct(id);
            return ResponseEntity.ok(ApiResponse.success(product, "Product featured successfully"));
            
        } catch (Exception e) {
            log.error("Failed to feature product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to feature product"));
        }
    }
    
    @PatchMapping("/{id}/unfeature")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Unfeature product", description = "Remove featured status from product (Admin only)")
    public ResponseEntity<ApiResponse<Product>> unfeatureProduct(@PathVariable String id) {
        try {
            Product product = productService.unfeatureProduct(id);
            return ResponseEntity.ok(ApiResponse.success(product, "Product unfeatured successfully"));
            
        } catch (Exception e) {
            log.error("Failed to unfeature product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to unfeature product"));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
            
        } catch (Exception e) {
            log.error("Failed to delete product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get product categories", description = "Retrieve all distinct product categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        try {
            List<String> categories = productService.getDistinctCategories();
            return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve categories: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve categories"));
        }
    }
    
    @GetMapping("/brands")
    @Operation(summary = "Get product brands", description = "Retrieve all distinct product brands")
    public ResponseEntity<ApiResponse<List<String>>> getBrands() {
        try {
            List<String> brands = productService.getDistinctBrands();
            return ResponseEntity.ok(ApiResponse.success(brands, "Brands retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve brands: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve brands"));
        }
    }
}
