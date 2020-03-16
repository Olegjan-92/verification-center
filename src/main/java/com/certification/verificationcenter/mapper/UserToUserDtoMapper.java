package com.certification.verificationcenter.mapper;

import com.certification.verificationcenter.dto.UserDto;
import com.certification.verificationcenter.model.User;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserToUserDtoMapper extends Function<User, UserDto> {
}
