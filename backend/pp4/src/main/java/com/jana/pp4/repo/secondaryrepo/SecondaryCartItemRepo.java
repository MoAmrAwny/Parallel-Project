package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.Cart;
import com.jana.pp4.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondaryCartItemRepo extends JpaRepository<CartItem, Integer> {
    @Query("SELECT ci "
            + "FROM CartItem ci "
            + "WHERE ci.cart.cart_id = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Integer cartId);
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cart_id = :cartId AND ci.itemId = :itemId")
    Optional<CartItem> findByCartIdAndItemId(@Param("cartId") Integer cartId,
                                             @Param("itemId") Integer itemId);


    @Modifying
    @Transactional("bookTransactionManager")
    @Query("DELETE FROM CartItem c WHERE c.cart.cart_id = :cartId")
    void deleteByCartId(@Param("cartId") Integer cartId);

    @Modifying
    @Transactional("bookTransactionManager")      // or whatever your primary TM bean is named
    @Query("DELETE FROM CartItem c WHERE c.cart.cart_id = :cartId AND c.itemId = :itemId")
    void deleteByCartIdAndItemId(@Param("cartId") Integer cartId,
                                 @Param("itemId")  Integer itemId);

}
