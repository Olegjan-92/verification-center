package com.certification.verificationcenter.service;

import com.certification.verificationcenter.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity add(User user);

    ResponseEntity getAllUsers();

    ResponseEntity deleteUserById(long id);

    ResponseEntity updateUser(User user);

    ResponseEntity getById(long id);

}
