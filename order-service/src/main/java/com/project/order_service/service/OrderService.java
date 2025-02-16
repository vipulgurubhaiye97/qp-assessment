package com.project.order_service.service;

import com.project.order_service.exception.OrderNotFoundException;
import com.project.order_service.exception.ResourceNotFoundException;
import com.project.order_service.model.GroceryItem;
import com.project.order_service.model.Order;
import com.project.order_service.model.OrderItem;
import com.project.order_service.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final WebClient webClient;

    public OrderService(OrderRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/groceries")
                .defaultHeaders(headers -> headers.setBasicAuth("admin", "admin123"))
                .build();
    }

    @Transactional
    public ResponseEntity<Order> placeOrder(Order order) {
        List<Long> missingIds = new ArrayList<>();  // List to collect missing grocery IDs

        // First loop: Check for missing grocery items and insufficient stock
        for (OrderItem item : order.getItems()) {
            GroceryItem groceryItem = getGroceryItem(item.getGroceryId());

            if (groceryItem == null) {
                missingIds.add(item.getGroceryId());  // Add missing ID to the list
            } else if (groceryItem.getQuantity() < item.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Insufficient stock
            }
        }

        // If there are missing items, throw a single ResourceNotFoundException with all IDs
        if (!missingIds.isEmpty()) {
            String missingIdsMessage = String.join(", ", missingIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
            throw new ResourceNotFoundException("Grocery items not found with IDs: " + missingIdsMessage);
        }

        // Second loop: Proceed with decreasing stock and saving order if everything is fine
        for (OrderItem item : order.getItems()) {
            String stockReduced = decreaseStock(item.getGroceryId(), item.getQuantity());
            if (!Objects.equals(stockReduced, "Stock updated successfully")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Stock update failed
            }
        }

        repository.save(order);  // Save the order after stock is reduced
        return ResponseEntity.status(HttpStatus.CREATED).body(order);  // Return 201 Created with the order
    }

    @Transactional
    public ResponseEntity<String> deleteOrderById(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // Restore stock of items before deletion
        for (OrderItem item : order.getItems()) {
            increaseStock(item.getGroceryId(), item.getQuantity());
        }

        repository.deleteById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
    }

    public Order getOrder(Long id) {
        return repository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    private GroceryItem getGroceryItem(Long id) {
        try {
            return webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(GroceryItem.class)
                    .block();  // Synchronously wait for the result
        } catch (Exception ex) {
            return null;  // Return null if the item is not found or any error occurs
        }
    }

    private String decreaseStock(Long id, int quantity) {
        return webClient.put()
                .uri("/{id}/decrease-stock?quantity={quantity}", id, quantity)
                .retrieve()
                .toEntity(String.class) // Retrieves the ResponseEntity<String>
                .mapNotNull(ResponseEntity::getBody) // Extracts the body from ResponseEntity
                .block(); // Blocks to return the value synchronously
    }

    private void increaseStock(Long id, int quantity) {
        webClient.put()
                .uri("/{id}/increase-stock?quantity={quantity}", id, quantity)
                .retrieve()
                .toEntity(String.class) // Retrieves the ResponseEntity<String>
                .mapNotNull(ResponseEntity::getBody) // Extracts the body from ResponseEntity
                .block();
    }
}