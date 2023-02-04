package com.progress.finalproject.controller;

import com.progress.finalproject.model.user.User;
import com.progress.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

import java.util.Optional;


@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("showForm", 1);
        modelAndView.setViewName("/registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        Optional<User> resultUser = userService.findByEmail(user.getEmail());
        if (userService.isCustomer(resultUser)) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided. Please log in");
        }
        if (userService.isStaff(resultUser)) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "Administrators are not allowed to purchase. Please use another email.");
        }
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/registration");
            modelAndView.addObject("showForm", 1);
        } else {
            userService.registerUser(userService.getUserPasswordMap(user,true));
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.addObject("showForm", 0);
            modelAndView.setViewName("/registration");
        }
        return modelAndView;
    }
}
