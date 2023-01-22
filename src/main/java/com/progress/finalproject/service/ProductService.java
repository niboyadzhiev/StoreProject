package com.progress.finalproject.service;

import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.product.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    Optional<Product> findById(Long id);

    Page<Product> findAllProductsPageable(Pageable pageable);

    Page<Product> findAllActiveProductsPageable(Pageable pageable);
    List<Product> findAllProducts();
    Page<Product> searchProductsByText(String searchParam1, String searchParam2, Pageable pageable);
    void saveProduct(Product product);
    void updateProductImages(Product inputProduct, Product productDb);
    void setProductImagesToNewProduct(Product product, long newId);
    void createNewProduct(Product product);
    void updateProduct(Product inputProduct, Optional<Product> productDb, boolean isDeleted);


}
