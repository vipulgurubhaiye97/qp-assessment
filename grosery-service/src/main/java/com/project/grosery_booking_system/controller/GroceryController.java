package com.project.grosery_booking_system.controller;

import com.project.grosery_booking_system.model.GroceryItem;
import com.project.grosery_booking_system.service.GroceryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groceries")
class GroceryController {
    private final GroceryService service;

    public GroceryController(GroceryService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getGroceryItemById(@PathVariable Long id) {
        return service.getGroceryById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAllItems() {
        return service.getAllItems();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GroceryItem> addItem(@RequestBody GroceryItem item) {
        return service.addItem(item);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return service.deleteItem(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroceryItem> updateGrocery(@PathVariable Long id, @RequestBody GroceryItem updatedGrocery) throws Exception {
        return service.updateGrocery(id, updatedGrocery);
    }

    // Get stock availability for a specific item
    @GetMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> checkStock(@PathVariable Long id) {
        return service.checkStock(id);
    }

    // Decrease stock when an order is placed
    @PutMapping("/{id}/decrease-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        return service.decreaseStock(id, quantity);
    }

    // Increase stock when inventory is replenished
    @PutMapping("/{id}/increase-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        return service.increaseStock(id, quantity);
    }
}
