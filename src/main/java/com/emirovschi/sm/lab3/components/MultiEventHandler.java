package com.emirovschi.sm.lab3.components;

import java.util.ArrayList;
import java.util.List;

public class MultiEventHandler
{
    private final List<Runnable> handlers;

    public MultiEventHandler()
    {
        handlers = new ArrayList<>();
    }

    public void onEvent(final Runnable handler)
    {
        this.handlers.add(handler);
    }

    public void trigger()
    {
        handlers.forEach(Runnable::run);
    }
}
