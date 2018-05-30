package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.Bead;

public class EventProcessor extends Bead
{
    private final Runnable runnable;

    public EventProcessor(final Runnable runnable)
    {
        this.runnable = runnable;
    }

    @Override
    protected void messageReceived(final Bead bead)
    {
        runnable.run();
    }
}
