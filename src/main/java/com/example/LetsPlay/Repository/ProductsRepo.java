package com.example.LetsPlay.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.LetsPlay.Model.Products;


public interface ProductsRepo extends JpaRepository<Products, Long> {

    Optional<Products> findByName(String name);

    @Query("SELECT p FROM Products p WHERE p.owner = :owner")
    List<Products> findByOwner(String owner);
    
}
