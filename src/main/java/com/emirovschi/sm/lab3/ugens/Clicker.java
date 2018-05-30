package com.emirovschi.sm.lab3.ugens;

import com.emirovschi.sm.lab3.AudioPlayerView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.SamplePlayer;

public class Clicker extends SamplePlayer
{
    private boolean added;
    private boolean enabled;

    public Clicker(final AudioContext audioContext)
    {
        super(audioContext, SampleManager.sample(AudioPlayerView.class.getResource("/click.mp3").getPath()));
        setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
        setKillOnEnd(false);
        added = false;
    }

    @Override
    protected void messageReceived(final Bead bead)
    {
        if (enabled && bead instanceof Clock)
        {
            if (!added)
            {
                added = true;
                context.out.addInput(this);
            }
            else
            {
                reset();
            }
        }
    }

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }
}
