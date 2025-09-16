package com.example.ecommerce.order;

import com.example.ecommerce.user.UserRepository;
import com.example.ecommerce.cart.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    
    public OrderController(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var order = orderRepository.findById(id).orElseThrow();
        
        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.forbidden().build();
        }
        
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> checkout(Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var cartItems = cartRepository.findByUserId(user.getId());
        
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }
        
        // Calculate total
        BigDecimal total = cartItems.stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Create order items
        List<OrderItem> orderItems = cartItems.stream()
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                return orderItem;
            })
            .collect(Collectors.toList());
        
        order.setItems(orderItems);
        orderRepository.save(order);
        
        // Clear cart
        cartRepository.deleteByUserId(user.getId());
        
        return ResponseEntity.ok(Map.of(
            "orderId", order.getId(),
            "amount", total,
            "message", "Order created successfully"
        ));
    }
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable Long id, @RequestBody PaymentRequest request, Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var order = orderRepository.findById(id).orElseThrow();
        
        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.forbidden().build();
        }
        
        order.setPaymentId(request.getPaymentId());
        order.setStatus(Order.OrderStatus.CONFIRMED);
        orderRepository.save(order);
        
        return ResponseEntity.ok(Map.of("message", "Payment confirmed"));
    }
    
    public static class PaymentRequest {
        private String paymentId;
        
        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    }
}