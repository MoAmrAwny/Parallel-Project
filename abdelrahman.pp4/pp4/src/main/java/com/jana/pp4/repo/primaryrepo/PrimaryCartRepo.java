package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrimaryCartRepo extends JpaRepository<Cart,Integer> {
    @Query("SELECT MAX(i.cart_id) FROM Cart i")
    Integer findMaxId();
}
