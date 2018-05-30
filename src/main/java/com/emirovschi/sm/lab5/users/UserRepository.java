package com.emirovschi.sm.lab5.users;

import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long>
{
    UserModel findByEmail(final String email);
}
