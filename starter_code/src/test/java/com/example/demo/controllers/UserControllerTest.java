package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerTest {
    private final UserService userService = mock(UserService.class);
    private final CartService cartService = mock(CartService.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private UserController userController;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userService", userService);
        TestUtils.injectObjects(userController, "cartService", cartService);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    }

    @Test
    public void should_be_able_to_register_a_new_valid_user() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User u = responseEntity.getBody();
        assert u != null;
        assertNotNull(u);

        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void a_valid_user_should_have_valid_id() {
        User mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setId(1);
        Mockito.<Optional<Object>>when(Optional.of(userService.findById(1L))).thenReturn(Optional.of(mockUser));
        User u = userController.findById(1L).getBody();
        assert u != null;
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertNotEquals(0, u.getId());
    }

    @Test
    public void given_a_valid_user_name_should_yield_to_valid_user() {
        User mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setId(1);
        when(userService.findByUsername("test")).thenReturn(mockUser);
        User u = userController.findByUserName("test").getBody();
        assert u != null;
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertNotNull("test", u.getUsername());
        Long nonExistingId = 1000l;
        assertNull( userController.findById(nonExistingId).getBody());

    }

}
