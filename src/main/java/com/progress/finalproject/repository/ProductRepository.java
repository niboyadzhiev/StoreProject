package com.progress.finalproject.repository;

import com.progress.finalproject.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(long id);

    Page<Product> findByDeletedAtIsNull (Pageable pageable);


    Page<Product> findAllByProductNameContainingIgnoreCaseOrProductDescContainingIgnoreCase(String searchParam1, String searchParam2, Pageable pageable);


}
