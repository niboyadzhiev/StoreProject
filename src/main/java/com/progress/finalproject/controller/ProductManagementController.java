package com.progress.finalproject.controller;


import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.repository.ProductImageRepository;
import com.progress.finalproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Controller
public class ProductManagementController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageRepository productImageRepository;

    @RequestMapping(value = "/productManagement", method = RequestMethod.GET)
    public ModelAndView listAllProducts(){
        ModelAndView modelAndView = new ModelAndView();
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("allProducts", productList);
        modelAndView.setViewName("/productManagement");

        return modelAndView;
    }

    @PostMapping("/productUpdate")
    public ModelAndView updateOrderStatus (@ModelAttribute Product product, @RequestParam(value = "isDeleted", required = false) boolean isDeleted){
        Optional<Product> productDb = productService.findById(product.getProductId());
        productDb.ifPresent((p)->{
            p.setProductName(product.getProductName());
            p.setProductDesc(product.getProductDesc());
            p.setDetailedDesc(product.getDetailedDesc());
            p.setProductPrice(product.getProductPrice());
            p.setVat(product.getVat());
            p.setAvailableUnits(product.getAvailableUnits());
            productService.updateProductImages(product,p);
            if(isDeleted) {
                p.setDeletedAt(Timestamp.from(Instant.now()));
            } else {
                p.setDeletedAt(null);
            }
            productService.saveProduct(p);
        });

        return listAllProducts();
    }


    @GetMapping ("/newProduct")
    public String newProduct () {
        return "/newProduct";
    }
    @PostMapping("/createProduct")
    public ModelAndView createProduct (@ModelAttribute Product product) {
        productService.createNewProduct(product);
        return listAllProducts();
    }

}
