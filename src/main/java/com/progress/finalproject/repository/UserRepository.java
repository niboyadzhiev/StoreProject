package com.progress.finalproject.repository;

import com.progress.finalproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRoleRoleNameNotOrderByRoleIdDesc(String roleName);

    Optional<User> findOneByUserId(long userId);

}
