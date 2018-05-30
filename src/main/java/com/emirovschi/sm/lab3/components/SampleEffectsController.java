package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.Echo;
import com.emirovschi.sm.lab3.ugens.LFOEffect;
import com.emirovschi.sm.lab3.ugens.StereoPanner;
import com.emirovschi.sm.lab3.ugens.ToggleUGen;
import com.emirovschi.sm.lab3.ugens.UGenChain;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

public class SampleEffectsController
{
    private final FileManager fileManager;

    private final Gain volume;
    private final ToggleUGen<Echo> echoUGen;
    private final ToggleUGen<LFOEffect> lfoUGen;
    private final StereoPanner stereoPanner;

    public SampleEffectsController(final FileManager fileManager, final UGenChain chain)
    {
        this.fileManager = fileManager;
        this.volume = new Gain(chain.getAudioContext(), 2, 1);
        this.echoUGen = new ToggleUGen<>(new Echo(chain.getAudioContext()));
        this.lfoUGen = new ToggleUGen<>(new LFOEffect(chain.getAudioContext()));
        this.stereoPanner = new StereoPanner(chain.getAudioContext());

        chain.addUGen(volume);
        chain.addUGen(stereoPanner);
        chain.addAdditionalUGen(echoUGen);
        chain.addUGen(lfoUGen);
    }

    public void toggleDirection(final int directionIndex)
    {
        final SamplePlayer.LoopType loopType = directionIndex == 0
                ? SamplePlayer.LoopType.NO_LOOP_FORWARDS
                : SamplePlayer.LoopType.NO_LOOP_BACKWARDS;

        fileManager.getPlayer().setLoopType(loopType);
    }

    public void toggleEcho(final boolean enabled)
    {
        echoUGen.setEnabled(enabled);
    }

    public void toggleLFO(final boolean enabled)
    {
        lfoUGen.setEnabled(enabled);
    }

    public void setPitch(final double pitch)
    {
        fileManager.getPlayer().setPitch(staticValue(rescaleSlider(pitch)));
    }

    public void setRate(final double rate)
    {
        fileManager.getPlayer().setRate(staticValue(rescaleSlider(rate)));
    }

    private float rescaleSlider(final double value)
    {
        return (float) (Math.abs(value) < 0.01 ? 1 : value > 0 ? 1 + value : 1 + 0.7 * value);
    }

    private Static staticValue(final float value)
    {
        return new Static(fileManager.getPlayer().getContext(), value);
    }

    public void setEchoDelay(final double delay)
    {
        echoUGen.getUGen().setDelay(10000 * (float) delay);
    }

    public void setEchoGain(final double gain)
    {
        echoUGen.getUGen().setGain((float) gain);
    }

    public void setLFOFrequency(final double frequency)
    {
        lfoUGen.getUGen().setFrequency(10 * (float) frequency);
    }

    public void setLFOGain(final double gain)
    {
        lfoUGen.getUGen().setGain((float) gain);
    }

    public void setBalance(final double balance)
    {
        stereoPanner.setBalance((float) balance);
    }

    public void setVolume(final double volume)
    {
        this.volume.setGain((float) volume);
    }
}
