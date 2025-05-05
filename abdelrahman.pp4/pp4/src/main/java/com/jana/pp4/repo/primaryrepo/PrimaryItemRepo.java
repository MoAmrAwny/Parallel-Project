package com.jana.pp4.repo.primaryrepo;

import com.jana.pp4.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimaryItemRepo extends JpaRepository<Item, Integer> {
    List<Item> findBySeller_AccIdAndAvailable(int sellerId, Boolean available);
}
