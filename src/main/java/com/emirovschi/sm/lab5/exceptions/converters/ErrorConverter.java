package com.emirovschi.sm.lab5.exceptions.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.exceptions.dto.ErrorDTO;
import org.springframework.stereotype.Component;

@Component
public class ErrorConverter implements Converter<Exception, ErrorDTO>
{
    @Override
    public ErrorDTO convert(final Exception exception)
    {
        final ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(exception.getClass().getSimpleName());
        errorDTO.setDescription(exception.getMessage());
        return errorDTO;
    }
}
