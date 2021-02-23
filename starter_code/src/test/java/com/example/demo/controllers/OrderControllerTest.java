package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
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
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderControllerTest {
    @Autowired
    private OrderController orderController;
    @Autowired
    private UserController userController;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    private User loggedInUser;

    private static Cart CreateMockCart() {
        Cart mockCart = new Cart();
        mockCart.addItem(new Item(1L, "Eggs", new BigDecimal("1.23"), "Large Eggs"));
        mockCart.addItem(new Item(2L, "Ripe Bananas 5 Pack", new BigDecimal("0.79"), "Ripen At Home Bananas 5 Pack"));
        return mockCart;

    }

    @Before
    public void Setup() {
        itemRepository.saveAll(ItemControllerTest.CreateMockItems());

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        loggedInUser = responseEntity.getBody();
        assert loggedInUser != null;
        assertNotNull(loggedInUser);
        assertEquals("test", loggedInUser.getUsername());
    }

    @Test
    public void given_user_an_order_can_be_submitted_and_fail_without_user() throws NoSuchFieldException, IllegalAccessException {

        Cart cart = CreateMockCart();
        cart.setUser(loggedInUser);
        cartRepository.save(cart);
        loggedInUser.setCart(cart);
        userRepository.save(loggedInUser);
        orderController.submit(loggedInUser.getUsername());
        List<UserOrder> orders = orderController.getOrdersForUser(loggedInUser.getUsername()).getBody();
        assertEquals(1, orders.size());
        //Negative test
        HttpStatus status = orderController.submit("invalidUser").getStatusCode();
        Assert.assertSame(HttpStatus.NOT_FOUND, status);

    }

    @Test
    public void get_order_for_valid_user_and_fail_with_invalid_user() {

        Cart cart = CreateMockCart();
        cart.setUser(loggedInUser);
        cartRepository.save(cart);
        loggedInUser.setCart(cart);
        userRepository.save(loggedInUser);
        orderController.submit(loggedInUser.getUsername());
        List<UserOrder> orders = orderController.getOrdersForUser(loggedInUser.getUsername()).getBody();
        assertEquals(1, orders.size());

        UserOrder firstOrder = orders.get(0);

        assertTrue(firstOrder.getId() > 0);
        assertNotNull(firstOrder.getUser());
        assertTrue(firstOrder.getTotal().doubleValue() > 0);


        assertEquals(2, orders.get(0).getItems().size());
        Item item = new Item(1L, "Eggs", new BigDecimal("1.23"), "Large Eggs");
        assertEquals(item, orders.get(0).getItems().get(0));
        HttpStatus status = orderController.getOrdersForUser("invalidUser").getStatusCode();
        Assert.assertSame(HttpStatus.NOT_FOUND, status);
    }

}
