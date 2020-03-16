package com.certification.verificationcenter.dto;

import com.certification.verificationcenter.model.Role;
import com.certification.verificationcenter.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String surName;
    private List<Role> roles;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setSurName(user.getSurName());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
