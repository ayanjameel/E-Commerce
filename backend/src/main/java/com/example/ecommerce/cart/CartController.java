package com.example.ecommerce.cart;

import com.example.ecommerce.user.UserRepository;
import com.example.ecommerce.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public CartController(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(cartRepository.findByUserId(user.getId()));
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var product = productRepository.findById(request.getProductId()).orElseThrow();
        
        var existingItem = cartRepository.findByUserIdAndProductId(user.getId(), request.getProductId());
        
        if (existingItem.isPresent()) {
            var item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartRepository.save(item);
        } else {
            var cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartRepository.save(cartItem);
        }
        
        return ResponseEntity.ok(Map.of("message", "Added to cart"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestBody UpdateCartRequest request, Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var cartItem = cartRepository.findById(id).orElseThrow();
        
        if (!cartItem.getUser().getId().equals(user.getId())) {
            return ResponseEntity.forbidden().build();
        }
        
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cartItem);
        
        return ResponseEntity.ok(Map.of("message", "Cart updated"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id, Authentication auth) {
        var user = userRepository.findByEmail(auth.getName()).orElseThrow();
        var cartItem = cartRepository.findById(id).orElseThrow();
        
        if (!cartItem.getUser().getId().equals(user.getId())) {
            return ResponseEntity.forbidden().build();
        }
        
        cartRepository.delete(cartItem);
        return ResponseEntity.ok(Map.of("message", "Removed from cart"));
    }
    
    public static class AddToCartRequest {
        private Long productId;
        private Integer quantity = 1;
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class UpdateCartRequest {
        private Integer quantity;
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}