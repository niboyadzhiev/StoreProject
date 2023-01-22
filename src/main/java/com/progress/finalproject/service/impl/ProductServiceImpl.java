package com.progress.finalproject.service.impl;

import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.repository.ProductImageRepository;
import com.progress.finalproject.repository.ProductRepository;
import com.progress.finalproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }


    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }


    @Override
    public Page<Product> findAllProductsPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findAllActiveProductsPageable(Pageable pageable) {
        return productRepository.findByDeletedAtIsNull(pageable);

    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> searchProductsByText(String searchParam1, String searchParam2, Pageable pageable) {
        return productRepository.findAllByProductNameContainingOrProductDescContaining(searchParam1, searchParam2, pageable);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.saveAndFlush(product);
    }

    @Override
    public void updateProductImages(Product inputProduct, Product productDb) {
        int inputIndex = 0;
        int dbIndex = 0;
        while (inputIndex < inputProduct.getImages().size() && dbIndex < productDb.getImages().size()) {
            inputProduct.getImages().get(inputIndex).setProductId(productDb.getProductId());
            productImageRepository.saveAndFlush(inputProduct.getImages().get(inputIndex));
            inputIndex++;
            dbIndex++;
        }
        while (dbIndex < productDb.getImages().size()) {
            productImageRepository.deleteById((long) dbIndex);
            dbIndex++;
        }
        while (inputIndex < inputProduct.getImages().size()) {
            inputProduct.getImages().get(inputIndex).setProductId(productDb.getProductId());
            productImageRepository.saveAndFlush(inputProduct.getImages().get(inputIndex));
            inputIndex++;
        }


    }

    @Override
    public void setProductImagesToNewProduct(Product inputProduct, long newId) {

        int inputIndex = 0;
        while (inputIndex < inputProduct.getImages().size()) {
            inputProduct.getImages().get(inputIndex).setProductId(newId);
            productImageRepository.saveAndFlush(inputProduct.getImages().get(inputIndex));
            inputIndex++;
        }

    }

    @Override
    public void createNewProduct(Product product) {
        product.setCreatedAt(Timestamp.from(Instant.now()));
        productRepository.saveAndFlush(product);
        setProductImagesToNewProduct(product, product.getProductId());
    }

    @Override
    public void updateProduct(Product inputProduct, Optional<Product> productDb, boolean isDeleted) {
        productDb.ifPresent((p)->{
            p.setProductName(inputProduct.getProductName());
            p.setProductDesc(inputProduct.getProductDesc());
            p.setDetailedDesc(inputProduct.getDetailedDesc());
            p.setProductPrice(inputProduct.getProductPrice());
            p.setVat(inputProduct.getVat());
            p.setAvailableUnits(inputProduct.getAvailableUnits());
            updateProductImages(inputProduct,p);
            if(isDeleted) {
                p.setDeletedAt(Timestamp.from(Instant.now()));
            } else {
                p.setDeletedAt(null);
            }
            saveProduct(p);
        });
    }


}



