package com.emirovschi.sm.lab5.users.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.users.dto.UserDTO;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserReverseConverter implements Converter<UserDTO, UserModel>
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel convert(final UserDTO userDTO)
    {
        final UserModel user = new UserModel();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }
}
