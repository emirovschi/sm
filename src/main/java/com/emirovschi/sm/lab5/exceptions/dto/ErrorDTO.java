package com.emirovschi.sm.lab5.exceptions.dto;

public class ErrorDTO
{
    private String error;
    private String description;

    public String getError()
    {
        return error;
    }

    public void setError(final String error)
    {
        this.error = error;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }
}
