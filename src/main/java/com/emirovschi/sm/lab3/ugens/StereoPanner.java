package com.emirovschi.sm.lab3.ugens;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

import java.util.Arrays;
import java.util.stream.IntStream;

public class StereoPanner extends UGen
{
    private static final int ROOT_SIZE = 1024;
    private static final Float[] ROOTS = buildRoots();
    private float leftGain;
    private float rightGain;

    public StereoPanner(final AudioContext audioContext)
    {
        super(audioContext, 2, 2);
        this.outputInitializationRegime = OutputInitializationRegime.RETAIN;
        this.outputPauseRegime = OutputPauseRegime.ZERO;
        setBalance(0);
    }

    @Override
    public synchronized void addInput(final UGen uGen)
    {
        super.addInput(uGen);
    }

    @Override
    public void calculateBuffer()
    {
        bufOut = new float[][]{new float[bufferSize], new float[bufferSize]};

        for (int i = 0; i < bufferSize; i++)
        {
            bufOut[0][i] = leftGain * bufIn[0][i];
            bufOut[1][i] = rightGain * bufIn[1][i];
        }
    }

    public void setBalance(final float balance)
    {
        if (balance >= 1.0F)
        {
            this.leftGain = 0.0F;
            this.rightGain = 1.0F;
        }
        else if (balance <= -1.0F)
        {
            this.leftGain = 1.0F;
            this.rightGain = 0.0F;
        }
        else
        {
            final float f1 = (balance + 1.0F) * 0.5F * (float) ROOT_SIZE;
            int n1 = (int) Math.floor((double) f1);
            final float f2 = f1 - n1;
            this.rightGain = ROOTS[n1] * (1.0F - f2) + ROOTS[n1 + 1] * f2;
            this.leftGain = ROOTS[ROOT_SIZE - n1] * (1.0F - f2) + ROOTS[ROOT_SIZE - (n1 + 1)] * f2;
        }
    }

    private static Float[] buildRoots()
    {
        return IntStream.range(0, ROOT_SIZE + 1)
                .boxed()
                .map(i -> (float) Math.sqrt((float) i / ROOT_SIZE))
                .toArray(Float[]::new);
    }
}
