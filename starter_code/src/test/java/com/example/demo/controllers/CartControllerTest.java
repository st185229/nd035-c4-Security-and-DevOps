package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.ItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CartControllerTest {
    @Autowired
    private CartController cartController;
    @Autowired
    private UserController userController;
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    private Cart cart;
    private User user;

    private static Cart CreateMockCart(User user) {
        Cart mockCart = new Cart();
        mockCart.addItem(new Item(1L, "Eggs", new BigDecimal("1.23"), "Large Eggs"));
        mockCart.addItem(new Item(2L, "Ripe Bananas 5 Pack", new BigDecimal("0.79"), "Ripen At Home Bananas 5 Pack"));
        mockCart.setUser(user);
        return mockCart;

    }

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        itemService.saveAllInventoryItems(ItemControllerTest.CreateMockItems());

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        user = responseEntity.getBody();
        assert user != null;
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        cart = CreateMockCart(user);
        cartService.save(cart);
    }

    @Test
    public void given_an_item_should_be_able_to_add_to_cart() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        Cart cart = cartController.addToCart(modifyCartRequest).getBody();
        assertNotNull(cart.getId());
        assert (cart.getItems().size() > 0);
        assertTrue(cart.getTotal().doubleValue() > 0);
    }

    @Test
    public void given_an_item_should_fail_add_to_cart_not_purchased_by_user() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        HttpStatus status = cartController.addToCart(modifyCartRequest).getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    @Test
    public void given_no_user_add_item_to_cart_should_fail() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        ResponseEntity<Cart> responseEntity = cartController.addToCart(modifyCartRequest);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }

    @Test
    public void given_item_existing_in_a_cart_should_be_removable() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        Cart cart = cartController.addToCart(modifyCartRequest).getBody();
        assertEquals(5, cart.getItems().size());
        assertEquals(new BigDecimal("3.95"), cart.getTotal());
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(1);
        cart = cartController.removeFromCart(modifyCartRequest).getBody();
        assertEquals(4, cart.getItems().size());

    }

    @Test
    public void remove_a_non_existing_item_should_fail_with_not_found_status() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(100L);
        modifyCartRequest.setQuantity(5);
        HttpStatus status = cartController.removeFromCart(modifyCartRequest).getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, status);

    }


}
