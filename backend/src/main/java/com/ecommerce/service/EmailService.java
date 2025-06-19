package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name:EcommerceHub}")
    private String appName;
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;
    
    // Simple email methods
    @Async
    public CompletableFuture<Void> sendSimpleEmail(String to, String subject, String text) {
        try {
            log.info("Sending simple email to: {}", to);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Simple email sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    @Async
    public CompletableFuture<Void> sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            log.info("Sending HTML email to: {}", to);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    @Async
    public CompletableFuture<Void> sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            log.info("Sending template email to: {} using template: {}", to, templateName);
            
            Context context = new Context();
            context.setVariables(variables);
            
            String htmlContent = templateEngine.process(templateName, context);
            
            return sendHtmlEmail(to, subject, htmlContent);
            
        } catch (Exception e) {
            log.error("Failed to send template email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    // User-related emails
    @Async
    public CompletableFuture<Void> sendWelcomeEmail(User user) {
        log.info("Sending welcome email to user: {}", user.getEmail());
        
        Map<String, Object> variables = Map.of(
                "userName", user.getFullName(),
                "appName", appName,
                "frontendUrl", frontendUrl
        );
        
        return sendTemplateEmail(
                user.getEmail(),
                "Welcome to " + appName,
                "welcome-email",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendEmailVerification(User user, String verificationToken) {
        log.info("Sending email verification to user: {}", user.getEmail());
        
        String verificationUrl = frontendUrl + "/verify-email?token=" + verificationToken;
        
        Map<String, Object> variables = Map.of(
                "userName", user.getFullName(),
                "verificationUrl", verificationUrl,
                "appName", appName
        );
        
        return sendTemplateEmail(
                user.getEmail(),
                "Verify your email address",
                "email-verification",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendPasswordResetEmail(User user, String resetToken) {
        log.info("Sending password reset email to user: {}", user.getEmail());
        
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        
        Map<String, Object> variables = Map.of(
                "userName", user.getFullName(),
                "resetUrl", resetUrl,
                "appName", appName
        );
        
        return sendTemplateEmail(
                user.getEmail(),
                "Reset your password",
                "password-reset",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendPasswordChangedNotification(User user) {
        log.info("Sending password changed notification to user: {}", user.getEmail());
        
        Map<String, Object> variables = Map.of(
                "userName", user.getFullName(),
                "appName", appName,
                "frontendUrl", frontendUrl
        );
        
        return sendTemplateEmail(
                user.getEmail(),
                "Password changed successfully",
                "password-changed",
                variables
        );
    }
    
    // Order-related emails
    @Async
    public CompletableFuture<Void> sendOrderConfirmationEmail(Order order) {
        log.info("Sending order confirmation email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "orderUrl", frontendUrl + "/orders/" + order.getOrderNumber(),
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Order Confirmation - " + order.getOrderNumber(),
                "order-confirmation",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendOrderStatusUpdateEmail(Order order) {
        log.info("Sending order status update email for order: {}", order.getOrderNumber());
        
        String statusMessage = getStatusMessage(order.getStatus());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "statusMessage", statusMessage,
                "orderUrl", frontendUrl + "/orders/" + order.getOrderNumber(),
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Order Update - " + order.getOrderNumber(),
                "order-status-update",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendOrderShippedEmail(Order order) {
        log.info("Sending order shipped email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "trackingNumber", order.getTrackingNumber(),
                "orderUrl", frontendUrl + "/orders/" + order.getOrderNumber(),
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Order Shipped - " + order.getOrderNumber(),
                "order-shipped",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendOrderDeliveredEmail(Order order) {
        log.info("Sending order delivered email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "reviewUrl", frontendUrl + "/orders/" + order.getOrderNumber() + "/review",
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Order Delivered - " + order.getOrderNumber(),
                "order-delivered",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendOrderCancelledEmail(Order order) {
        log.info("Sending order cancelled email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "orderUrl", frontendUrl + "/orders/" + order.getOrderNumber(),
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Order Cancelled - " + order.getOrderNumber(),
                "order-cancelled",
                variables
        );
    }
    
    // Payment-related emails
    @Async
    public CompletableFuture<Void> sendPaymentSuccessEmail(Order order) {
        log.info("Sending payment success email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "orderUrl", frontendUrl + "/orders/" + order.getOrderNumber(),
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Payment Successful - " + order.getOrderNumber(),
                "payment-success",
                variables
        );
    }
    
    @Async
    public CompletableFuture<Void> sendPaymentFailedEmail(Order order) {
        log.info("Sending payment failed email for order: {}", order.getOrderNumber());
        
        Map<String, Object> variables = Map.of(
                "userName", order.getUser().getFullName(),
                "order", order,
                "retryUrl", frontendUrl + "/orders/" + order.getOrderNumber() + "/payment",
                "appName", appName
        );
        
        return sendTemplateEmail(
                order.getUser().getEmail(),
                "Payment Failed - " + order.getOrderNumber(),
                "payment-failed",
                variables
        );
    }
    
    // Newsletter and promotional emails
    @Async
    public CompletableFuture<Void> sendNewsletterEmail(String email, String subject, String content) {
        log.info("Sending newsletter email to: {}", email);
        
        Map<String, Object> variables = Map.of(
                "content", content,
                "unsubscribeUrl", frontendUrl + "/unsubscribe?email=" + email,
                "appName", appName
        );
        
        return sendTemplateEmail(email, subject, "newsletter", variables);
    }
    
    @Async
    public CompletableFuture<Void> sendPromotionalEmail(String email, String subject, String content, String ctaUrl) {
        log.info("Sending promotional email to: {}", email);
        
        Map<String, Object> variables = Map.of(
                "content", content,
                "ctaUrl", ctaUrl,
                "unsubscribeUrl", frontendUrl + "/unsubscribe?email=" + email,
                "appName", appName
        );
        
        return sendTemplateEmail(email, subject, "promotional", variables);
    }
    
    // Admin notification emails
    @Async
    public CompletableFuture<Void> sendLowStockAlert(String productName, String productSku, int currentStock) {
        log.info("Sending low stock alert for product: {}", productSku);
        
        // In a real implementation, you would have admin email addresses configured
        String adminEmail = "admin@" + appName.toLowerCase() + ".com";
        
        String subject = "Low Stock Alert - " + productName;
        String content = String.format(
                "Product %s (SKU: %s) is running low on stock. Current stock: %d",
                productName, productSku, currentStock
        );
        
        return sendSimpleEmail(adminEmail, subject, content);
    }
    
    @Async
    public CompletableFuture<Void> sendNewOrderAlert(Order order) {
        log.info("Sending new order alert for order: {}", order.getOrderNumber());
        
        String adminEmail = "admin@" + appName.toLowerCase() + ".com";
        
        String subject = "New Order Received - " + order.getOrderNumber();
        String content = String.format(
                "A new order has been placed.\nOrder Number: %s\nCustomer: %s\nTotal: $%.2f",
                order.getOrderNumber(), order.getUser().getFullName(), order.getTotal()
        );
        
        return sendSimpleEmail(adminEmail, subject, content);
    }
    
    // Helper methods
    private String getStatusMessage(com.ecommerce.entity.enums.OrderStatus status) {
        switch (status) {
            case PENDING:
                return "Your order is pending confirmation.";
            case CONFIRMED:
                return "Your order has been confirmed and is being prepared.";
            case PROCESSING:
                return "Your order is being processed.";
            case SHIPPED:
                return "Your order has been shipped.";
            case OUT_FOR_DELIVERY:
                return "Your order is out for delivery.";
            case DELIVERED:
                return "Your order has been delivered.";
            case CANCELLED:
                return "Your order has been cancelled.";
            case REFUNDED:
                return "Your order has been refunded.";
            default:
                return "Your order status has been updated.";
        }
    }
}
