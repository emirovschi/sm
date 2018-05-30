package com.emirovschi.sm.lab5.converters;

public interface Populator<T, R>
{
    void populate(T source, R target);
}
