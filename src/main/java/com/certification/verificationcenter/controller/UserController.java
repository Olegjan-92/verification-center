package com.certification.verificationcenter.controller;

import com.certification.verificationcenter.model.User;
import com.certification.verificationcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity getById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") long id) {
        return userService.deleteUserById(id);
    }

    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
