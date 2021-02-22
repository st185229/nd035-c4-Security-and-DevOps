package com.example.demo.service;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public void save(UserOrder order){

        orderRepository.save(order);
        log.debug("The order saved order={}",order);
    }
    public List<UserOrder> findByUser(User user) {
        log.debug("find user being queried={}",user);
        return orderRepository.findByUser(user);
    }
}
