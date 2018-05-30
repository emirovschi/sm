package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.common.Component;
import com.emirovschi.sm.lab3.ugens.Clicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Static;

public class Metronom extends Component<HBox>
{
    private final Slider metronomSlider;
    private final CheckBox metronom;
    private final Clock clock;
    private final AudioContext audioContext;
    private final Clicker clicker;

    public Metronom(final AudioContext audioContext)
    {
        super("Metronom sound", new HBox());
        this.audioContext = audioContext;

        metronom = new CheckBox("Enabled");
        metronomSlider = new Slider(40, 200, 100);
        clock = new Clock(audioContext);
        clock.setIntervalEnvelope(new Static(audioContext, 60000));
        clock.setTicksPerBeat(40);

        clicker = new Clicker(audioContext);
        clock.addMessageListener(clicker);
        clock.setClick(true);
        audioContext.out.addDependent(clock);

        metronom.setOnAction(event -> toggleMetronom());
        metronomSlider.setMajorTickUnit(1);
        metronomSlider.setDisable(true);
        metronomSlider.valueProperty().addListener((observer, o, n) -> setFrequency((double) n));

        add(metronom);
        add(metronomSlider);

        HBox.setHgrow(metronomSlider, Priority.ALWAYS);
        getContent().setSpacing(10);
    }

    private void setFrequency(final double frequency)
    {
        clock.setTicksPerBeat(Math.round((float) frequency));
    }

    private void toggleMetronom()
    {
        metronomSlider.setDisable(!metronom.isSelected());
        clicker.setEnabled(metronom.isSelected());
    }
}
