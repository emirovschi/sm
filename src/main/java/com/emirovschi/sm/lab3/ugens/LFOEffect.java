package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Phasor;

public class LFOEffect extends UGen
{
    private final Phasor phasor;
    private final Gain gain;
    private final Gain gain2;

    public LFOEffect(final AudioContext audioContext)
    {
        super(audioContext, 2, 2);

        phasor = new Phasor(audioContext);
        gain = new Gain(audioContext, 2, phasor);
        gain2 = new Gain(audioContext, 2, 0);
        gain2.addInput(gain);
    }

    @Override
    public synchronized void addInput(final UGen uGen)
    {
        super.addInput(uGen);
        gain.addInput(uGen);
    }

    @Override
    public void calculateBuffer()
    {
        phasor.update();
        gain.update();
        gain2.update();

        bufOut = new float[][]{ gain2.getOutBuffer(0), gain2.getOutBuffer(1) };
    }

    public void setFrequency(final float frequency)
    {
        phasor.setFrequency(frequency);
    }

    public void setGain(final float gain)
    {
        this.gain2.setGain(gain);
    }
}
