package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

import java.util.ArrayList;
import java.util.List;

public class UGenChain
{
    private final AudioContext audioContext;
    private final List<UGen> prevUGens;
    private UGen ugen;

    public UGenChain(final AudioContext audioContext)
    {
        this.audioContext = audioContext;
        this.prevUGens = new ArrayList<>();
    }

    public void addAdditionalUGen(final UGen uGen)
    {
        if (this.ugen != null)
        {
            uGen.addInput(this.ugen);
        }

        prevUGens.add(uGen);
    }

    public void addUGen(final UGen ugen)
    {
        if (!prevUGens.isEmpty())
        {
            prevUGens.forEach(ugen::addInput);
        }
        prevUGens.clear();
        prevUGens.add(ugen);
        this.ugen = ugen;
    }

    public List<UGen> getPrevUGens()
    {
        return prevUGens;
    }

    public AudioContext getAudioContext()
    {
        return audioContext;
    }

    public UGen getUGen()
    {
        return ugen;
    }
}
