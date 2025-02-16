package com.project.grosery_booking_system.service;

import com.project.grosery_booking_system.exception.ResourceNotFoundException;
import com.project.grosery_booking_system.model.GroceryItem;
import com.project.grosery_booking_system.repository.GroceryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroceryService {
    private final GroceryRepository repository;

    public GroceryService(GroceryRepository repository) {
        this.repository = repository;
    }

    // Get grocery by ID
    public ResponseEntity<GroceryItem> getGroceryById(Long id) {
        GroceryItem grocery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));
        return ResponseEntity.ok(grocery);  // Return 200 OK with the grocery item
    }

    // Get all items
    public ResponseEntity<List<GroceryItem>> getAllItems() {
        List<GroceryItem> items = repository.findAll();
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // No items found, return 204 No Content
        }
        return ResponseEntity.ok(items);  // Return 200 OK with the list
    }

    // Add a new item
    public ResponseEntity<GroceryItem> addItem(GroceryItem item) {
        GroceryItem savedItem = repository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);  // Return 201 Created with the saved item
    }

    // Delete an item
    public ResponseEntity<Void> deleteItem(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Return 204 No Content on successful delete
        } else {
            throw new ResourceNotFoundException("Grocery item not found with id: " + id);
        }
    }

    // Update grocery item
    public ResponseEntity<GroceryItem> updateGrocery(Long id, GroceryItem updatedGrocery) {
        GroceryItem grocery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        grocery.setName(updatedGrocery.getName());
        grocery.setPrice(updatedGrocery.getPrice());
        grocery.setQuantity(updatedGrocery.getQuantity());  // Update stock quantity

        GroceryItem savedGrocery = repository.save(grocery);
        return ResponseEntity.status(HttpStatus.OK).body(savedGrocery);  // Return 200 OK with the updated item
    }

    // Decrease stock when an order is placed
    public ResponseEntity<String> decreaseStock(Long id, int quantity) {
        GroceryItem grocery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        if (grocery.getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough stock available");
        }

        grocery.setQuantity(grocery.getQuantity() - quantity);
        repository.save(grocery);
        return ResponseEntity.status(HttpStatus.OK).body("Stock updated successfully");
    }

    // Increase stock when new inventory is added
    public ResponseEntity<String> increaseStock(Long id, int quantity) {
        GroceryItem grocery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        grocery.setQuantity(grocery.getQuantity() + quantity);
        repository.save(grocery);
        return ResponseEntity.status(HttpStatus.OK).body("Stock replenished successfully");
    }

    // Check stock availability before placing an order
    public ResponseEntity<Integer> checkStock(Long id) {
        GroceryItem grocery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        return ResponseEntity.status(HttpStatus.OK).body(grocery.getQuantity());
    }
}
