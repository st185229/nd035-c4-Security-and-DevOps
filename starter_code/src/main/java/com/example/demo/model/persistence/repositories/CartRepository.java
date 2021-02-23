package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
//	Cart findByUser(User user);
}
