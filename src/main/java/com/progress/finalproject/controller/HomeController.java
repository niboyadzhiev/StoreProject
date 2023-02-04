package com.progress.finalproject.controller;

import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.service.ProductService;
import com.progress.finalproject.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import java.util.Optional;

@Controller
public class HomeController {


    static final int INITIAL_PAGE = 0;
    @Value("${pictures.directory}")
    private String imagesFolder;
    static final int PAGE_SIZE = 5;
    private final ProductService productService;
    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView findProducts(@RequestParam(value = "searchTerm", required = false) String searchTerm, @RequestParam("page") Optional<Integer> page) throws IOException {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<Product> products;
        if (searchTerm == null) {
            products = productService.findAllActiveProductsPageable(PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            products = productService.searchProductsByText(searchTerm,searchTerm, PageRequest.of(evalPage, PAGE_SIZE));
        }
        Pager pager = new Pager(products);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("searchTerm", searchTerm);
        modelAndView.addObject("pager", pager);
        modelAndView.setViewName("/home");
        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView home(@RequestParam("id") long productId) {
        Optional<Product> product = productService.findById(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product", product.get());
        modelAndView.setViewName("/details");
        return modelAndView;
    }

    @RequestMapping(value = "/image/{path}", method = RequestMethod.GET)
    public @ResponseBody byte[] getImage(@PathVariable String path) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(imagesFolder + path + ".jpg"));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }





}
