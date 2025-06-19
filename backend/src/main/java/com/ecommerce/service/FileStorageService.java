package com.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {
    
    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;
    
    @Value("${file.upload.max-size:10485760}") // 10MB
    private long maxFileSize;
    
    @Value("${file.upload.allowed-types:jpg,jpeg,png,gif,webp,pdf}")
    private String allowedTypes;
    
    private static final List<String> IMAGE_TYPES = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final List<String> DOCUMENT_TYPES = Arrays.asList("pdf", "doc", "docx", "txt");
    
    // File upload operations
    public FileUploadResult uploadFile(MultipartFile file, String category) {
        try {
            log.info("Uploading file: {} to category: {}", file.getOriginalFilename(), category);
            
            // Validate file
            validateFile(file);
            
            // Create upload directory if it doesn't exist
            Path uploadPath = createUploadDirectory(category);
            
            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            
            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Generate file URL
            String fileUrl = generateFileUrl(category, fileName);
            
            log.info("File uploaded successfully: {}", fileName);
            
            return new FileUploadResult(
                    true,
                    fileName,
                    fileUrl,
                    file.getSize(),
                    getFileExtension(file.getOriginalFilename()),
                    null
            );
            
        } catch (Exception e) {
            log.error("Failed to upload file {}: {}", file.getOriginalFilename(), e.getMessage());
            return new FileUploadResult(
                    false,
                    null,
                    null,
                    0,
                    null,
                    e.getMessage()
            );
        }
    }
    
    public FileUploadResult uploadProductImage(MultipartFile file) {
        return uploadFile(file, "products");
    }
    
    public FileUploadResult uploadCategoryImage(MultipartFile file) {
        return uploadFile(file, "categories");
    }
    
    public FileUploadResult uploadUserAvatar(MultipartFile file) {
        return uploadFile(file, "avatars");
    }
    
    public FileUploadResult uploadDocument(MultipartFile file) {
        return uploadFile(file, "documents");
    }
    
    // Multiple file upload
    public List<FileUploadResult> uploadMultipleFiles(MultipartFile[] files, String category) {
        log.info("Uploading {} files to category: {}", files.length, category);
        
        return Arrays.stream(files)
                .map(file -> uploadFile(file, category))
                .toList();
    }
    
    // File deletion operations
    public boolean deleteFile(String fileName, String category) {
        try {
            log.info("Deleting file: {} from category: {}", fileName, category);
            
            Path filePath = Paths.get(uploadDir, category, fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", fileName);
                return true;
            } else {
                log.warn("File not found for deletion: {}", fileName);
                return false;
            }
            
        } catch (IOException e) {
            log.error("Failed to delete file {}: {}", fileName, e.getMessage());
            return false;
        }
    }
    
    public boolean deleteFileByUrl(String fileUrl) {
        try {
            // Extract category and filename from URL
            String[] urlParts = fileUrl.split("/");
            if (urlParts.length >= 2) {
                String category = urlParts[urlParts.length - 2];
                String fileName = urlParts[urlParts.length - 1];
                return deleteFile(fileName, category);
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to delete file by URL {}: {}", fileUrl, e.getMessage());
            return false;
        }
    }
    
    // File validation
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size: " + maxFileSize + " bytes");
        }
        
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isAllowedFileType(fileExtension)) {
            throw new IllegalArgumentException("File type not allowed: " + fileExtension);
        }
        
        // Additional validation for images
        if (isImageFile(fileExtension)) {
            validateImageFile(file);
        }
    }
    
    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }
        
        // Additional image validation can be added here
        // e.g., checking image dimensions, format validation, etc.
    }
    
    // File utility methods
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    
    private boolean isAllowedFileType(String extension) {
        List<String> allowedExtensions = Arrays.asList(allowedTypes.split(","));
        return allowedExtensions.contains(extension.toLowerCase());
    }
    
    private boolean isImageFile(String extension) {
        return IMAGE_TYPES.contains(extension.toLowerCase());
    }
    
    private boolean isDocumentFile(String extension) {
        return DOCUMENT_TYPES.contains(extension.toLowerCase());
    }
    
    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + "." + extension;
    }
    
    private Path createUploadDirectory(String category) throws IOException {
        Path uploadPath = Paths.get(uploadDir, category);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath);
        }
        
        return uploadPath;
    }
    
    private String generateFileUrl(String category, String fileName) {
        // In a real application, this would be the public URL to access the file
        // e.g., "https://yourdomain.com/uploads/products/filename.jpg"
        return "/uploads/" + category + "/" + fileName;
    }
    
    // File information methods
    public boolean fileExists(String fileName, String category) {
        Path filePath = Paths.get(uploadDir, category, fileName);
        return Files.exists(filePath);
    }
    
    public long getFileSize(String fileName, String category) {
        try {
            Path filePath = Paths.get(uploadDir, category, fileName);
            return Files.size(filePath);
        } catch (IOException e) {
            log.error("Failed to get file size for {}: {}", fileName, e.getMessage());
            return 0;
        }
    }
    
    public String getFileContentType(String fileName) {
        String extension = getFileExtension(fileName);
        
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }
    
    // Cleanup operations
    public void cleanupOldFiles(String category, int daysOld) {
        try {
            log.info("Cleaning up files older than {} days in category: {}", daysOld, category);
            
            Path categoryPath = Paths.get(uploadDir, category);
            
            if (!Files.exists(categoryPath)) {
                return;
            }
            
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
            
            Files.walk(categoryPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toInstant()
                                    .isBefore(cutoffDate.atZone(java.time.ZoneId.systemDefault()).toInstant());
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            log.debug("Deleted old file: {}", path.getFileName());
                        } catch (IOException e) {
                            log.error("Failed to delete old file {}: {}", path.getFileName(), e.getMessage());
                        }
                    });
            
            log.info("Cleanup completed for category: {}", category);
            
        } catch (IOException e) {
            log.error("Failed to cleanup old files in category {}: {}", category, e.getMessage());
        }
    }
    
    // File upload result class
    public static class FileUploadResult {
        private final boolean success;
        private final String fileName;
        private final String fileUrl;
        private final long fileSize;
        private final String fileType;
        private final String errorMessage;
        
        public FileUploadResult(boolean success, String fileName, String fileUrl, long fileSize, String fileType, String errorMessage) {
            this.success = success;
            this.fileName = fileName;
            this.fileUrl = fileUrl;
            this.fileSize = fileSize;
            this.fileType = fileType;
            this.errorMessage = errorMessage;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getFileName() { return fileName; }
        public String getFileUrl() { return fileUrl; }
        public long getFileSize() { return fileSize; }
        public String getFileType() { return fileType; }
        public String getErrorMessage() { return errorMessage; }
    }
}
