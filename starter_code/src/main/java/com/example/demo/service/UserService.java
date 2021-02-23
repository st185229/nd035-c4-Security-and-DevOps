package com.example.demo.service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String userName) {
        log.debug("Find by user userName={}", userName);
        return userRepository.findByUsername(userName);
    }

    public Optional<User> findById(Long id) {
        log.debug("Find by user userId={}", id);
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
        log.debug("Find by user user={}", user);
    }
}
