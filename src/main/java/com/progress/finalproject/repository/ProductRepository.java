package com.progress.finalproject.repository;

import com.progress.finalproject.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(long id);

//    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    Page<Product> findByDeletedAtIsNull (Pageable pageable);


//    @Query("SELECT p FROM Product p WHERE p.productName LIKE ?1 OR p.productDesc LIKE ?1 OR p.detailedDesc LIKE ?1")
    Page<Product> findAllByProductNameContainingOrProductDescContaining(String searchParam1, String searchParam2, Pageable pageable);


}
