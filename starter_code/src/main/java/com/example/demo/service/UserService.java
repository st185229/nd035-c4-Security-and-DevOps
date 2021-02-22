package com.example.demo.service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername (String userName){
        return userRepository.findByUsername(userName);
    }

    public Optional<User> findById (Long id){
        return userRepository.findById(id);
    }

    public void  save(User user){
        userRepository.save(user);
    }

}
