package com.progress.finalproject.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "please provide an email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column (name = "first_name")
    @NotEmpty(message = "provide first name")
    private String firstName;

    @Column (name = "last_name")
    @NotEmpty(message = "provide last name")
    private String lastName;

    @Column(name = "phone")
    @NotEmpty(message = "please provide phone")
    private String phone;

    @Column (name = "registration_date")
    private Timestamp registrationDate;

    @Column (name = "deletedAt")
    private Timestamp deletedAt;

    @Column(name = "address")
    @NotEmpty(message = "please provide address")
    private String address;

    @Column(name = "role_id")
    private long roleId;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    private Role role;








}
