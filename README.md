# Grocery Booking System

## Overview
The Grocery Booking System is a microservices-based Spring Boot application designed for managing grocery items and bookings. It includes role-based access control (Admin and User) and is secured with Spring Security.

## Technologies Used
- **Java** (Spring Boot, Spring Security, Spring Data JPA)
- **Database**: PostgreSQL
- **Containerization**: Docker

## Microservices
- **Grocery Service**: Manages grocery items (CRUD operations).
- **Order Service**: Handles orders and stock management.

## API Endpoints

### Grocery Service
- **GET** `/api/groceries/{id}` - Fetch grocery item by ID
- **POST** `/api/groceries` - Create a new grocery item (**Admin only**)
- **PUT** `/api/groceries/{id}` - Update a grocery item (**Admin only**)
- **DELETE** `/api/groceries/{id}` - Delete a grocery item (**Admin only**)

### Order Service
- **POST** `/api/orders` - Place an order
- **PUT** `/api/orders/decrease-stock/{id}` - Reduce stock after purchase

## Security
- **Admins** have access to modification endpoints.
- **Users** can view grocery items and place orders.

## Future Enhancements
- Integrate **Keycloak** for authentication.
- Implement **API Gateway** for service routing.

