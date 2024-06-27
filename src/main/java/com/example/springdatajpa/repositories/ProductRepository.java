package com.example.springdatajpa.repositories;

import com.example.springdatajpa.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p, c from Product p join Category c on p.category.id = c.id where p.name like %:search%")
    List<Object> getAllProductWithAnnotation(String search);

}
