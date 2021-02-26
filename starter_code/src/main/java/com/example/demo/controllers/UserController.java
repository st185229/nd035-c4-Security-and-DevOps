package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/api/user")
public class UserController {
    public final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final CartService cartService;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, CartService cartService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            log.error("Invalid user");
            return ResponseEntity.notFound().build();
        }
        log.info("The user by userId={} is {}", id, user);
        return ResponseEntity.of(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            log.error("Invalid user");
            return ResponseEntity.notFound().build();
        }
        log.debug("The user with user name={} is {}", username, user);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        String userName = createUserRequest.getUsername();
        user.setUsername(userName);
        int passwordLength = createUserRequest.getPassword().length();
        if (passwordLength < 7) {
            log.debug("passwordLength={} chars, it should be at least 7 chars longs", passwordLength);
            log.debug("Failed creating user userName={}", userName);
            return ResponseEntity.badRequest().body(user);
        }
        if (!createUserRequest.getConfirmPassword().equals(createUserRequest.getPassword())) {
            log.debug("Password should match");
            log.debug("Failed creating user userName={}", userName);
            return ResponseEntity.badRequest().body(user);
        }
        Cart cart = new Cart();
        log.debug("User to be created userName={}", userName);
        cartService.save(cart);
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getConfirmPassword()));
        userService.save(user);
        log.debug("User userName={} is created successfully", userName);
        log.info("User created={}", user);
        return ResponseEntity.ok(user);
    }

}
