package com.emirovschi.sm.lab5.users.impl;

import com.emirovschi.sm.lab5.exceptions.NotFoundException;
import com.emirovschi.sm.lab5.users.UserRepository;
import com.emirovschi.sm.lab5.users.UserService;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<UserModel> getUserByEmail(final String email)
    {
        return ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public void save(final UserModel user)
    {
        userRepository.save(user);
    }

    @Override
    public UserModel getSessionUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
    }

    @Override
    public Set<UserModel> getUsers(final Set<String> users)
    {
        return users.stream().map(userRepository::findByEmail).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public UserModel getUserById(final long id)
    {
        return userRepository.findOne(id);
    }
}
