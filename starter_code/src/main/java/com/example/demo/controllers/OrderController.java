package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/order")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        log.info("Order being requested for the user ={}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            log.error("Invalid user for creating order userName={}", username);
            return ResponseEntity.notFound().build();
        }
        Cart cart = user.getCart();
        if(cart.getItems().isEmpty()){
            log.error("The cart is empty for userName={}", username);
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderService.save(order);
        log.info("A order has been created successfully with  id={} for the user ={} with total of {}",
                order.getId(), order.getUser(), order.getTotal());
        return ResponseEntity.ok(order);
    }
    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            log.error("Null user or no order available for user={}", username);
            return ResponseEntity.notFound().build();
        }
        log.info("user={}", user);
        return ResponseEntity.ok(orderService.findByUser(user));
    }
}
