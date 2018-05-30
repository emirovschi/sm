package com.emirovschi.sm.lab5.users.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.users.dto.UserDTO;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<UserModel, UserDTO>
{
    @Override
    public UserDTO convert(final UserModel user)
    {
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
