package com.emirovschi.sm.lab5.users;

import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.users.dto.UpdateUserDTO;
import com.emirovschi.sm.lab5.users.dto.UserDTO;
import com.emirovschi.sm.lab5.users.exceptions.UserAlreadyExistsException;

public interface UserFacade
{
    UserDTO getSessionUser();

    MediaDTO getAvatar(long id);

    void register(UserDTO user) throws UserAlreadyExistsException;

    void update(UpdateUserDTO user);
}
