package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import net.beadsproject.beads.ugens.BiquadFilter;

public class Band extends Slider
{
    private final BiquadFilter filter;

    public Band(final UGenChain chain, final float frequency, final float bandwidth)
    {
        super(-19, 19, 0);
        final float qFactor =  bandwidth / frequency;
        this.filter = new BiquadFilter(chain.getAudioContext(), 2, BiquadFilter.Type.PEAKING_EQ);
        this.filter.setFrequency(frequency);
        this.filter.setQ(qFactor);

        chain.addUGen(filter);

        valueProperty().addListener((observer, o, n) -> setGain((double) n));
        setOrientation(Orientation.VERTICAL);
    }

    private void setGain(final double gain)
    {
        filter.setGain((float) gain);
    }
}
