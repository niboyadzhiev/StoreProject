package com.progress.finalproject.controller;

import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.user.User;
import com.progress.finalproject.service.ProductService;
import com.progress.finalproject.service.ShoppingCartService;
import com.progress.finalproject.service.UserService;
import com.progress.finalproject.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.progress.finalproject.controller.HomeController.INITIAL_PAGE;
import static com.progress.finalproject.controller.HomeController.PAGE_SIZE;


@Controller
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    private final ProductService productService;

    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.userService = userService;
    }


    @GetMapping("/shoppingCart")
    public ModelAndView shoppingCart() {
        ModelAndView modelAndView = new ModelAndView("/shoppingCart");
        modelAndView.addObject("products", shoppingCartService.getProductsInCart());
        modelAndView.addObject("total", shoppingCartService.getTotal().toString());
        return modelAndView;
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public ModelAndView addProductToCart(@RequestParam(value = "searchTerm", required = false) String searchTerm, @RequestBody MultiValueMap<String, String> formData, @RequestParam("page") Optional<Integer> page, HttpServletRequest request) {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<Product> products;
        if (searchTerm == null) {
            products = productService.findAllActiveProductsPageable(PageRequest.of(evalPage, PAGE_SIZE));
        } else {
            products = productService.searchProductsByText(searchTerm, searchTerm, PageRequest.of(evalPage, PAGE_SIZE));
        }
        Pager pager = new Pager(products);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("searchTerm", searchTerm);
        modelAndView.addObject("pager", pager);

        Optional<Product> product = productService.findById(Long.parseLong(formData.get("productId").get(0)));
        shoppingCartService.addProduct(product.get(), Integer.parseInt(formData.get("number").get(0)));
        modelAndView.setViewName("/home");

        return modelAndView;

    }

    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public ModelAndView removeProductFromCart(@PathVariable("productId") Long productId) {
        productService.findById(productId).ifPresent(shoppingCartService::removeProduct);
        return shoppingCart();
    }

    @GetMapping("/shoppingCart/proceed")
    public ModelAndView proceed(Principal principal) {
        try {
            shoppingCartService.checkAvailability();
        } catch (NotEnoughProductsInStockException e) {
            return shoppingCart().addObject("outOfStockMessage", e.getMessage());
        }
        ModelAndView modelAndView = new ModelAndView();
        User user = null;
        if (principal != null) {
            user = userService.findByEmail(principal.getName()).get();
        } else {
            user = new User();
        }

        modelAndView.addObject("user", user);
        modelAndView.addObject("products", shoppingCartService.getProductsInCart());
        modelAndView.setViewName("/checkout");
        return modelAndView;
    }

    @RequestMapping(value = "/shoppingCart/checkout", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, @RequestParam(name = "register", required = false) boolean registerCheckbox, Principal principal) {
        Optional<User> resultUser = userService.findByEmail(user.getEmail());
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("products", shoppingCartService.getProductsInCart());
        if (userService.isCustomer(resultUser) && principal == null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided. Please log in");
        }

        if (userService.isStaff(resultUser)) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "Administrators are not allowed to purchase. Please use another email.");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/checkout");
            return modelAndView;
        }

        User userToUse = null;
        shoppingCartService.placeOrder(userService.getUserPasswordMap(user,registerCheckbox));

        modelAndView.addObject("successMessage", "Successful order. Thank you!");

        modelAndView.setViewName("/checkout");

        return modelAndView;
    }


//
//    @PostMapping("/shoppingCart/checkout")
//    public ModelAndView checkout(@RequestBody MultiValueMap<String, String> checkoutData) {
//        User user = new User();
//
//        for (Map.Entry<String, List<String>> param : checkoutData.entrySet()) {
//            System.out.println(param.getKey()+" "+ Arrays.toString(param.getValue().toArray()));
//        }
//        return new ModelAndView();
//    }


}
