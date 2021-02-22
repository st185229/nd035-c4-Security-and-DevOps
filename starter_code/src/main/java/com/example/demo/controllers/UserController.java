package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userService.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        String userName = createUserRequest.getUsername();
        user.setUsername(userName);
        Cart cart = new Cart();
        log.info("user name: " + userName);
        cartService.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() < 7
                || !createUserRequest.getConfirmPassword()
                .equals(createUserRequest.getPassword())) {
            ResponseEntity res = ResponseEntity.badRequest().build();
            return res;
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getConfirmPassword()));
        userService.save(user);
        return ResponseEntity.ok(user);
    }

}
