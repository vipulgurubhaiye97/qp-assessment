package com.project.order_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroceryItem {
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
