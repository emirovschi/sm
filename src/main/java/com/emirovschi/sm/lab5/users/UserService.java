package com.emirovschi.sm.lab5.users;

import com.emirovschi.sm.lab5.users.models.UserModel;

import java.util.Optional;
import java.util.Set;

public interface UserService
{
    Optional<UserModel> getUserByEmail(String email);

    void save(UserModel user);

    UserModel getSessionUser();

    Set<UserModel> getUsers(Set<String> users);

    UserModel getUserById(long id);
}
