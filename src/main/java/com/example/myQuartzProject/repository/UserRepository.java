package com.example.myQuartzProject.repository;

import com.example.myQuartzProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    Optional<User> findByUserNameOrEmail(String uName, String eMail);

    Optional<User> findByUserId(String userId);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    int countByGender(String gender);

}
