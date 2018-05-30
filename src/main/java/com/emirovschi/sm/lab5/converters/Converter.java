package com.emirovschi.sm.lab5.converters;

public interface Converter<T, R>
{
    R convert(T t);
}
