package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

public class Echo extends UGen
{
    private final TapIn tapIn;
    private final TapOut tapOut;
    private final Gain gain;

    public Echo(final AudioContext audioContext)
    {
        super(audioContext, 1, 1);

        tapIn = new TapIn(audioContext, 10000);
        tapOut = new TapOut(audioContext, tapIn, 0);
        gain = new Gain(audioContext, 1, 0);
        gain.addInput(tapOut);
    }

    @Override
    public synchronized void addInput(final UGen uGen)
    {
        super.addInput(uGen);
        tapIn.addInput(uGen);
    }

    @Override
    public void calculateBuffer()
    {
        tapIn.update();
        tapOut.update();
        gain.update();

        bufOut = new float[][]{ gain.getOutBuffer(0) };
    }

    public void setDelay(final float delay)
    {
        tapOut.setDelay(delay);
    }

    public void setGain(final float gain)
    {
        this.gain.setGain(gain);
    }
}
