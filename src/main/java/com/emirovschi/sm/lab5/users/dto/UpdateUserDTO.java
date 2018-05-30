package com.emirovschi.sm.lab5.users.dto;

import com.emirovschi.sm.lab5.users.validation.Equals;

import javax.validation.constraints.Size;

@Equals(property = "password", with = "password2", message = "passwords don't match")
public class UpdateUserDTO
{
    @Size(min = 3, max = 100)
    private String name;

    @Size(min = 6, max = 100)
    private String password;

    private String password2;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public String getPassword2()
    {
        return password2;
    }

    public void setPassword2(final String password2)
    {
        this.password2 = password2;
    }
}
