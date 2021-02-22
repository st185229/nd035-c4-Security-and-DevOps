package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@Log4j2
@RequestMapping("/api/cart")
public class CartController {
    private final UserService userService;
    private final CartService cartService;
    private final ItemService itemService;

    public CartController(UserService userService, CartService cartService, ItemService itemService) {
        this.userService = userService;
        this.cartService = cartService;
        this.itemService = itemService;
    }

    ////Suresh  Refactored the duplicate code
    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        log.info("addToCart request={}",request);
        log.debug("Add to cart userName={}, itemId={}, quantity={}",
                request.getUsername(),request.getItemId(), request.getQuantity());
        return updateCart(request, false);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        log.info("remove from cart request={}",request);
        log.debug("Modify cart cart userName={}, itemId={}, quantity={}",
                request.getUsername(),request.getItemId(), request.getQuantity());
        return updateCart(request, true);
    }

    private ResponseEntity<Cart> updateCart(ModifyCartRequest request, boolean remove) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            log.error("User does not exists userName={}, itemId={}, quantity={}",
                    request.getUsername(),request.getItemId(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemService.findInventoryItemById(request.getItemId());
        if (item.isEmpty()) {
            log.error("Item is not present userName={}, itemId={}, quantity={}",
                    request.getUsername(),request.getItemId(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        if (remove) {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.removeItem(item.get()));
        } else {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.addItem(item.get()));
        }
        cartService.save(cart);
        log.debug("Cart saved successfully cartId={}, user={},total={}"
                , cart.getId(), cart.getUser(), cart.getTotal());
        log.info("cart={}",cart);
        return ResponseEntity.ok(cart);

    }

}
