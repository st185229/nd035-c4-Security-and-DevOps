package com.example.demo.service;


import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(UserOrder order){
        orderRepository.save(order);
    }


    public List<UserOrder> findByUser(User user) {
        return orderRepository.findByUser(user);
    }


}
