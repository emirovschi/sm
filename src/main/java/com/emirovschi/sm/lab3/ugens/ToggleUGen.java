package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.UGen;

import java.util.stream.IntStream;

public class ToggleUGen<T extends UGen> extends UGen
{
    private final T uGen;
    private boolean enabled;

    public ToggleUGen(final T uGen)
    {
        super(uGen.getContext(), uGen.getIns(), uGen.getOuts());
        this.uGen = uGen;
        this.outputInitializationRegime = OutputInitializationRegime.RETAIN;
    }

    @Override
    public synchronized void addInput(final UGen uGen)
    {
        super.addInput(uGen);
        this.uGen.addInput(uGen);
    }

    @Override
    public void calculateBuffer()
    {
        if (enabled)
        {
            uGen.update();
            bufOut = new float[uGen.getOuts()][bufferSize];
            IntStream.range(0, uGen.getOuts()).forEach(i -> bufOut[i] = uGen.getOutBuffer(i));
        }
        else
        {
            bufOut = bufIn;
        }
    }

    public T getUGen()
    {
        return uGen;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }
}
