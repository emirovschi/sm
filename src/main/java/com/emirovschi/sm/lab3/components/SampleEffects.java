package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.Echo;
import com.emirovschi.sm.lab3.ugens.LFOEffect;
import com.emirovschi.sm.lab3.ugens.ToggleUGen;
import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

public class SampleEffects extends GridPane
{
    private final SampleEffectsController controller;

    private final ComboBox<String> direction;

    private final CheckBox lfo;
    private final CheckBox echo;

    private final Slider echoDelay;
    private final Slider echoGain;
    private final Slider lfoFrequency;
    private final Slider lfoGain;

    public SampleEffects(final FileManager fileManager, final SampleEffectsController controller)
    {
        this.controller = controller;

        direction = new ComboBox<>();
        direction.getItems().addAll("Forward", "Backwards");
        direction.getSelectionModel().select(0);
        direction.setMaxWidth(Double.MAX_VALUE);
        direction.setOnAction(event -> controller.toggleDirection(direction.getSelectionModel().getSelectedIndex()));

        lfo = new CheckBox("LFO");
        lfo.setOnAction(event -> updateLFO());
        echo = new CheckBox("Echo");
        echo.setOnAction(event -> updateEcho());

        final Slider volume = new Slider(0, 1, 1);
        volume.valueProperty().addListener((observable, o, n) -> controller.setVolume((double) n));
        final Slider pitch = new Slider(-1, 1, 0);
        pitch.valueProperty().addListener((observable, o, n) -> controller.setPitch((double) n));
        final Slider rate = new Slider(-1, 1, 0);
        rate.valueProperty().addListener((observable, o, n) -> controller.setRate((double) n));
        echoDelay = new Slider(0, 1, 0);
        echoDelay.valueProperty().addListener((observable, o, n) -> controller.setEchoDelay((double) n));
        echoGain = new Slider(0, 1, 0);
        echoGain.valueProperty().addListener((observable, o, n) -> controller.setEchoGain((double) n));
        lfoFrequency = new Slider(0, 1, 0);
        lfoFrequency.valueProperty().addListener((observable, o, n) -> controller.setLFOFrequency((double) n));
        lfoGain = new Slider(0, 1, 0);
        lfoGain.valueProperty().addListener((observable, o, n) -> controller.setLFOGain((double) n));
        final Slider balance = new Slider(-1, 1, 0);
        balance.valueProperty().addListener((observable, o, n) -> controller.setBalance((double) n));

        add(new Label("Volume"), 0, 0);
        add(volume, 1, 0, 2, 1);
        add(new Label("Direction"), 0, 1);
        add(direction, 1, 1, 2, 1);
        add(new Label("Pitch"), 0, 2);
        add(pitch, 1, 2, 2, 1);
        add(new Label("Rate"), 0, 3);
        add(rate, 1, 3, 2, 1);
        add(echo, 0, 4, 1, 2);
        add(new Label("Delay"), 1, 4);
        add(echoDelay, 2, 4);
        add(new Label("Gain"), 1, 5);
        add(echoGain, 2, 5);
        add(lfo, 0, 6);
        add(new Label("Frequency"), 1, 6);
        add(lfoFrequency, 2, 6);
        add(new Label("Gain"), 1, 7);
        add(lfoGain, 2, 7);
        add(new Label("Balance"), 0, 8);
        add(balance, 1, 8, 2, 1);

        getColumnConstraints().addAll(createConstrain(Priority.NEVER), createConstrain(Priority.NEVER), createConstrain(Priority.ALWAYS));

        setHgap(10);
        setVgap(10);

        setDisable(true);
        fileManager.onSampleChange(this::enable);
    }

    private ColumnConstraints createConstrain(final Priority priority)
    {
        final ColumnConstraints constraint = new ColumnConstraints();
        constraint.setHgrow(priority);
        return constraint;
    }

    private void enable()
    {
        setDisable(false);
        updateEcho();
        updateLFO();
    }

    private void updateEcho()
    {
        echoDelay.setDisable(!echo.isSelected());
        echoGain.setDisable(!echo.isSelected());
        controller.toggleEcho(echo.isSelected());
    }

    private void updateLFO()
    {
        lfoFrequency.setDisable(!lfo.isSelected());
        lfoGain.setDisable(!lfo.isSelected());
        controller.toggleLFO(lfo.isSelected());
    }
}
