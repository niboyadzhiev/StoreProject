package com.progress.finalproject.controller;

import com.progress.finalproject.model.user.User;
import com.progress.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;


@Controller
public class LoginController {
    @Autowired
    UserService userService;


    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "/login";
    }

    @GetMapping("/forgottenPassword")
    public ModelAndView forgottenPassword() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("showForm",1);
        modelAndView.setViewName("forgottenPassword");
        return modelAndView;
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forgottenPassword");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("showForm",1);
            return modelAndView;
        } else {
            modelAndView.addObject("showForm",0);
            modelAndView.addObject("successMessage", "New password has been sent successfully");
            userService.changePassword(user.getEmail());
        }
        return modelAndView;
    }

    @RequestMapping("/changePassword")
    public ModelAndView changePassword(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        userService.changePassword(principal.getName());
        modelAndView.addObject("successMessage", "New password has been sent successfully");
        modelAndView.setViewName("changePassword");
        return modelAndView;
    }

    @RequestMapping("/userManagement")
    public ModelAndView userManagement() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", userService.getAllRegisteredUsers());
        modelAndView.setViewName("userManagement");
        return modelAndView;
    }


    @PostMapping("/userUpdate")
    public ModelAndView updateUser(@ModelAttribute User user, @RequestParam(value = "isStaff", required = false) boolean isStaff ) {
        ModelAndView modelAndView = new ModelAndView();
        userService.updateUserRole(user,isStaff);
        modelAndView.addObject("users", userService.getAllRegisteredUsers());
        modelAndView.addObject("successMessage", "Successful update.");
        modelAndView.setViewName("userManagement");
        return modelAndView;
    }

    @GetMapping("/personalData")
    public ModelAndView personalData(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userService.findByEmail(principal.getName()).get());
        modelAndView.addObject("showForm",1);
        modelAndView.setViewName("personalData");
        return modelAndView;
    }

    @PostMapping("/updatePersonalData")
    public ModelAndView updatePersonalData(@ModelAttribute User user) {
        ModelAndView modelAndView = new ModelAndView();
        userService.updatePersonalData(user);
        modelAndView.addObject("user", user);
        modelAndView.addObject("successMessage", "Successful update of personal data.");
        modelAndView.addObject("showForm",0);
        modelAndView.setViewName("personalData");
        return modelAndView;
    }




}
