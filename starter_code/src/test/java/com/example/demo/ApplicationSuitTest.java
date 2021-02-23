package com.example.demo;

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.OrderControllerTest;
import com.example.demo.controllers.UserControllerTest;
import com.example.demo.security.UserAuthTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
/**
 * The below test suit can be executed to execute all tests and coverage
 */
@Suite.SuiteClasses({
        UserControllerTest.class,
        CartControllerTest.class,
        ItemControllerTest.class,
        OrderControllerTest.class,
        UserAuthTest.class
})
public class ApplicationSuitTest {
    @Test
    public void contextLoads() {
    }
}
