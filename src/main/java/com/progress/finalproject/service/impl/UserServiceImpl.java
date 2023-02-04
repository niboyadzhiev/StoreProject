package com.progress.finalproject.service.impl;

import com.progress.finalproject.model.user.User;
import com.progress.finalproject.repository.UserRepository;
import com.progress.finalproject.service.EmailService;
import com.progress.finalproject.service.UserService;
import com.progress.finalproject.util.CustomerPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerPasswordGenerator passwordGenerator;
    @Autowired
    EmailService emailService;


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllRegisteredUsers() {
        return userRepository.findByRoleRoleNameNotOrderByRoleIdDesc("walkin");
    }

    @Override
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }


    @Override
    public void setDefaultPassword(User user) {
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

    }

    @Override
    public void setRegistrationDate(User user) {
        user.setRegistrationDate(Timestamp.from(Instant.now()));
    }


    @Override
    public User returnWalkinCustomer(User user) {
        Optional<User> resultUser = userRepository.findByEmail(user.getEmail());
        resultUser.ifPresent((theUser) -> {
            theUser.setFirstName(user.getFirstName());
            theUser.setLastName(user.getLastName());
            theUser.setAddress(user.getAddress());
            theUser.setPhone(user.getPhone());
        });
        return resultUser.orElseGet(() -> {
            setDefaultPassword(user);
            setRegistrationDate(user);
            user.setRoleId(3);
            return user;
        });
    }


    @Override
     public String generatePassword() {
        return passwordGenerator.generatePassayPassword();
    }

    @Override
    public String encodePassword(String originalPassword) {
        return passwordEncoder.encode(originalPassword);
    }

    @Override
    public boolean isCustomer(Optional<User> user) {
        return user.isPresent() && user.get().getRole().getRoleName().equals(CUSTOMER);
    }

    @Override
    public boolean isStaff(Optional<User> user) {
        return user.isPresent() && user.get().getRole().getRoleName().equals(STAFF);
    }

    @Override
    public Map<User, String> getUserPasswordMap(User user, boolean registerCheckbox) {
        Map<User, String> map = new HashMap<>();
        Optional<User> resultUser = userRepository.findByEmail(user.getEmail());
        if (registerCheckbox) {
            resultUser.ifPresent((theUser) -> {
                map.put(theUser,generatePassword());
                theUser.setFirstName(user.getFirstName());
                theUser.setLastName(user.getLastName());
                theUser.setAddress(user.getAddress());
                theUser.setPhone(user.getPhone());
                theUser.setRoleId(1);
                theUser.setPassword(encodePassword(map.get(theUser)));
                setRegistrationDate(user);

            });

            resultUser.orElseGet(() -> {
                map.put(user,generatePassword());
                user.setPassword(encodePassword(map.get(user)));
                setRegistrationDate(user);
                user.setRoleId(1);
                return user;
            });
        } else {
            map.put(returnWalkinCustomer(user),null);
        }
        return map;
    }

    @Override
    public User getUserFromMap(Map<User, String> map) {
        User user = null;
        for (Map.Entry<User, String> entry : map.entrySet()) {
            user = entry.getKey();
        }
        return user;
    }

    @Override
    public String getPasswordFromMap(Map<User, String> map) {
        String pass = null;
        for (Map.Entry<User, String> entry : map.entrySet()) {
            pass = entry.getValue();
        }
        return pass;
    }

    @Override
    @Transactional
    public void registerUser(Map<User, String> userPasswordMap) {
        saveUser(getUserFromMap(userPasswordMap));
        emailService.sendEmail(getUserFromMap(userPasswordMap),getPasswordFromMap(userPasswordMap));

    }

    @Override
    @Transactional
    public void changePassword(String email) {
        findByEmail(email).ifPresent((u)->{
            if(!u.getRole().getRoleName().equals(WALKIN)) {
                String newPassword = generatePassword();
                u.setPassword(encodePassword(newPassword));
                emailService.sendNewPassword(u,newPassword);
            }
        });

    }
    @Override
    public void updateUserRole(User user, boolean isStaff) {
        userRepository.findOneByUserId(user.getUserId()).ifPresent((u)->{
            user.setRegistrationDate(u.getRegistrationDate());
            if (isStaff) {
                user.setRoleId(2);
            } else {
                user.setRoleId(1);
            }
        });
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updatePersonalData(User inputUser) {
        Optional<User> userDb = userRepository.findOneByUserId(inputUser.getUserId());
        userDb.ifPresent(u -> {
            u.setFirstName(inputUser.getFirstName());
            u.setLastName(inputUser.getFirstName());
            u.setEmail(inputUser.getEmail());
            u.setAddress(inputUser.getAddress());
            u.setPhone(inputUser.getPhone());
            saveUser(u);
        });
    }
}
