package com.project.order_service.controller;

import com.project.order_service.model.Order;
import com.project.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) { return service.placeOrder(order); }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) { return service.getOrder(id); }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")  // Only users with ADMIN role can access
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        return service.deleteOrderById(orderId);
    }
}