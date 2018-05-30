package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.common.Component;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Noise;

public class NoiseControl extends Component<HBox>
{
    private final Slider noiseGain;
    private final CheckBox noise;
    private final Noise noiseUGen;
    private final Gain gainUGen;
    private final AudioContext audioContext;

    public NoiseControl(final AudioContext audioContext)
    {
        super("Noise control", new HBox());
        noise = new CheckBox("Enabled");
        noiseGain = new Slider(0, 1, 0);
        noiseUGen = new Noise(audioContext);
        gainUGen = new Gain(audioContext, 1, 0);

        gainUGen.addInput(noiseUGen);
        this.audioContext = audioContext;

        noise.setOnAction(event -> toggleNoise());
        noiseGain.valueProperty().addListener((observer, o, n) -> setGain((double) n));

        add(noise);
        add(noiseGain);

        HBox.setHgrow(noiseGain, Priority.ALWAYS);
        getContent().setSpacing(10);
    }

    private void toggleNoise()
    {
        noiseGain.setDisable(!noise.isSelected());

        if (noise.isSelected())
        {
            audioContext.out.addInput(gainUGen);
        }
        else
        {
            audioContext.out.removeAllConnections(gainUGen);
        }
    }

    public void setGain(final double gain)
    {
        gainUGen.setGain((float) gain);
    }
}
