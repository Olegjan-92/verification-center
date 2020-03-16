package com.certification.verificationcenter.security;

import com.certification.verificationcenter.dao.UserRepository;
import com.certification.verificationcenter.model.User;
import com.certification.verificationcenter.security.jwt.JwtUser;
import com.certification.verificationcenter.security.jwt.JwtUserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);

        if(user == null){
            throw new UsernameNotFoundException("User with login: " + login + " not found");
        }

        JwtUser jwtClient = JwtUserFactory.create(user);
        log.info("In loadUserByUsername - user with login: {} successfully loaded", login);
        return jwtClient;
    }
}
