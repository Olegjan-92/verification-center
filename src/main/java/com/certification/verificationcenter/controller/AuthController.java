package com.certification.verificationcenter.controller;

import com.certification.verificationcenter.dao.UserRepository;
import com.certification.verificationcenter.dto.AuthenticationRequestDto;
import com.certification.verificationcenter.model.User;
import com.certification.verificationcenter.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {

        User userFromDb = userRepository.findByLogin(user.getLogin());
        if (userFromDb != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким логином уже существует!");
        }
//        user.setRole(UserRoles.USER);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping(produces = "application/json")
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String login = requestDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
            User user = userRepository.findByLogin(login);
            if (user == null) {
                throw new UsernameNotFoundException("User with login: " + login + " not found");
            }
            String token = jwtTokenProvider.createToken(login, user.getRoles());
            Map<Object, Object> resp = new HashMap<>();
            resp.put("login", login);
            resp.put("token", token);
            return ResponseEntity.ok(resp);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid login or password");
        }
    }
}
