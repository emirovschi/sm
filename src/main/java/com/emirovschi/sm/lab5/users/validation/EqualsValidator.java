package com.emirovschi.sm.lab5.users.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class EqualsValidator implements ConstraintValidator<Equals, Object>
{
    private String property;
    private String with;

    @Override
    public void initialize(final Equals equals)
    {
        property = equals.property();
        with = equals.with();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext constraintValidatorContext)
    {
        return Objects.equals(getField(object, property), getField(object, with));
    }

    private Object getField(final Object object, final String field)
    {
        try
        {
            return object.getClass().getDeclaredMethod("get" + capitalize(field)).invoke(object);
        }
        catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e)
        {
            return null;
        }
    }

    private String capitalize(final String field)
    {
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}
