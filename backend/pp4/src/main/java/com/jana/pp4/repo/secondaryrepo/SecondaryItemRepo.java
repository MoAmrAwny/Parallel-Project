package com.jana.pp4.repo.secondaryrepo;

import com.jana.pp4.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface SecondaryItemRepo extends JpaRepository<Item, Integer> {
    List<Item> findBySeller_AccIdAndAvailable(int sellerId, Boolean available);

    List<Item> findByCategoryAndAvailable(String category, Boolean available);
    List<Item> findByCategory(String category);
    List<Item> findByAvailable(Boolean available);
    List<Item> findByCondition(String condition);
    List<Item> findByPriceBetween(BigDecimal priceAfter, BigDecimal priceBefore);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.seller.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Item> searchItems(String keyword);


    @Query("SELECT MAX(i.itemId) FROM Item i")
    Integer findMaxId();
}
