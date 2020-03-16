package com.certification.verificationcenter.service.impl;

import com.certification.verificationcenter.dao.RoleRepository;
import com.certification.verificationcenter.dao.UserRepository;
import com.certification.verificationcenter.dto.UserDto;
import com.certification.verificationcenter.mapper.UserToUserDtoMapper;
import com.certification.verificationcenter.model.Role;
import com.certification.verificationcenter.model.User;
import com.certification.verificationcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserToUserDtoMapper userToUserDtoMapper;

    @Override
    public ResponseEntity add(User user) {
        User checkUser = userRepository.findByLogin(user.getLogin());
        if (checkUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с данным инн зарегистрирован ранее!");
        } else {
            Role roleUser = roleRepository.findByName("ROLE_USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList(roleUser));
            User saveUser = userRepository.save(user);
            log.info("In add - user : {} successfully added", saveUser);
            return ResponseEntity.status(HttpStatus.OK).body(saveUser);
        }
    }

    @Override
    public ResponseEntity getAllUsers() {
        List<User> allUsers = userRepository.findAll();

        log.info("In getAllUsers - {} users found", allUsers.size());
        return ResponseEntity.status(HttpStatus.OK).body(allUsers.stream()
                .map(userToUserDtoMapper)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity getById(long id) {
        Optional<User> searchUser = userRepository.findById(id);
        if (searchUser.isPresent()) {
            log.info("In getById - user : {} successfully found by login : {}", searchUser, id);
            return ResponseEntity.status(HttpStatus.OK).body(searchUser);
        } else {
            log.info("In getById - user not found by id : {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с ИНН = " + id + " не найден!");
        }
    }

    @Override
    public ResponseEntity deleteUserById(long id) {
        try {
            userRepository.deleteById(id);
            Map<String, String> message = new HashMap<>();
            message.put("message", "Клиент с id = " + id + " успешно удален!");
            log.info("In deleteUserById - user successfully deleted by id : {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            log.info("In deleteUserById - user not found by id : {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Клиент с id = " + id + " не найден, а следовательно не может быть удален!");
        }
    }

    @Override
    public ResponseEntity updateUser(User user) {
        Optional<User> userFromBase = userRepository.findById(user.getId());
        if (userFromBase.isPresent()) {
            User updatedUser = userRepository.save(user);
            log.info("In updateUser - user : {} successfully updated by id : {}", updatedUser, user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } else {
            log.info("In updateUser - user not found by id : {}", user.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("клиента с данным id не существует!");
        }
    }
}
