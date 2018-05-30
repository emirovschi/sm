package com.emirovschi.sm.lab5.users.dto;

import com.emirovschi.sm.lab5.users.validation.Equals;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Equals(property = "password", with = "password2", message = "passwords don't match")
public class UserDTO
{
    private Long id;

    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "invalid format")
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    private String password2;

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

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
