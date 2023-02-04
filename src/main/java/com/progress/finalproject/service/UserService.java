package com.progress.finalproject.service;

import com.progress.finalproject.model.user.User;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    public static final String CUSTOMER = "customer";
    public static final String STAFF = "staff";
    public static final String WALKIN = "walkin";

    Optional<User> findByEmail(String email);

    List<User> getAllRegisteredUsers();

    User saveUser(User user);


    void setDefaultPassword(User user);

    void setRegistrationDate(User user);

    User returnWalkinCustomer(User user);

//    User returnCustomer(User user);

    String generatePassword();

    String encodePassword(String originalPassword);

    boolean isCustomer(Optional<User> user);
    boolean isStaff(Optional<User> user);

    Map<User, String> getUserPasswordMap(User user, boolean registerCheckbox);

    User getUserFromMap (Map<User, String> map);
    String getPasswordFromMap (Map<User, String> map);
    void registerUser(Map<User, String> userPasswordMap);

    void changePassword(String email);

    void updateUserRole(User user, boolean isStaff);

    void updatePersonalData(User inputUser);
}
