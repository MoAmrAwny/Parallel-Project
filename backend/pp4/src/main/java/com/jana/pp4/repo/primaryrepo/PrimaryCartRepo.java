package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.Account;
import com.jana.pp4.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrimaryCartRepo extends JpaRepository<Cart, Integer> {
    @Query("SELECT MAX(i.cart_id) FROM Cart i")
    Integer findMaxId();

    Optional<Cart> findByCartOwner_AccId(Integer accountId);

}
