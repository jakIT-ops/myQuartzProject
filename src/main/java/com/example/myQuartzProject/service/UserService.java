package com.example.myQuartzProject.service;

import com.example.myQuartzProject.dto.user.CreateUserRequest;
import com.example.myQuartzProject.dto.user.GetUserInfoResponse;
import com.example.myQuartzProject.dto.user.GetUserResponse;
import com.example.myQuartzProject.entity.User;
import com.example.myQuartzProject.exception.RunTimeExceptionPlaceHolder;
import com.example.myQuartzProject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService{
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public void usersStats() {
        List<User> users = userRepository.findAll();

        int activeUser = 0;
        int inactiveUser = 0;
        int maleUser = 0;
        int femaleUser = 0;

        for (User user: users) {
            if(user.isActive()) {
                activeUser++;

                if(user.getGender() == "male") {
                    maleUser++;
//                    maleUser = userRepository.countByGender("male");
                } else {
                    femaleUser++;
//                    femaleUser = userRepository.countByGender("female");
                }
            } else {
                inactiveUser++;
            }
        }

        log.info("Хэрэглэгчийн статус:");
        log.info("==============");
        log.info("Идэвхтэй хэрэглэгчийн тоо: {}", activeUser);
        log.info(" - Эрэгтэй хэрэглэгчийн тоо: {}", maleUser);
        log.info(" - Эмэгтэй хэрэглэгчийн тоо: {}", femaleUser);
        log.info("Идэвхгүй хэрэглэгчийн тоо: {}", inactiveUser);
        log.info("==========================");



//        int
    }

    public List<User> getAllAccount(){
        return userRepository.findAll();
    }


    public String createUser(CreateUserRequest createUserRequest) throws RunTimeExceptionPlaceHolder {

        if (userRepository.existsByUserName(createUserRequest.getUserName())) {
            throw new RunTimeExceptionPlaceHolder("Username is already taken!!");
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new RunTimeExceptionPlaceHolder("Email address already in use!!");
        }

        // encrypt password
        String encryptedPassword = createUserRequest.getPassword();
        try {
            encryptedPassword = hashPassword(createUserRequest.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }
        logger.info("Encrypted password:", encryptedPassword);

        User user = new User(createUserRequest.getUserName(), encryptedPassword,
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                null,
                new Date(),
                new Date(),
                createUserRequest.isActive(),
                createUserRequest.getGender()
        );

        User savedUser = userRepository.save(user);
//
//        if (!errorResponse.getErrors().isEmpty()) {
//            throw new SuccessCodeWithErrorResponse(savedUser.getUserId(), errorResponse);
//        }

        return savedUser.getUserId();
    }

    public GetUserResponse getUserByUserName(String userName) {
        Optional<User> userNameOrEmailOptional = userRepository
                .findByUserNameOrEmail(userName, userName);
        User userByUserName = userNameOrEmailOptional.orElseThrow(() ->
                new RunTimeExceptionPlaceHolder("UserName or Email doesn't exist!!")
        );

        GetUserResponse user = new GetUserResponse(userByUserName.getUserId(),
                userByUserName.getUserName(),
                userByUserName.getFirstName(),
                userByUserName.getLastName(),
                userByUserName.getEmail());
        return user;
    }


    public GetUserResponse getUserByUserId(String userId) {
        Optional<User> userIdOptional = userRepository.findByUserId(userId);
        User userById = userIdOptional.orElseThrow(() ->
                new RunTimeExceptionPlaceHolder("UserName or Email doesn't exist!!")
        );

        GetUserResponse user = new GetUserResponse(userById.getUserId(),
                userById.getUserName(),
                userById.getFirstName(),
                userById.getLastName(),
                userById.getEmail());
        return user;
    }


    public GetUserInfoResponse getUserInfo(String userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = (String) authentication.getPrincipal();

        GetUserResponse userByUserId = getUserByUserId(userId);
//
        GetUserInfoResponse user = new GetUserInfoResponse(
                userByUserId.getUserId(),
                userByUserId.getUserName(),
                userByUserId.getFirstName(),
                userByUserId.getLastName(),
                userByUserId.getEmail());
        return user;
    }

    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
}
