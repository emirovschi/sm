package com.emirovschi.sm.lab3;

import com.emirovschi.sm.lab3.components.AdvancedSamplePlayer;
import com.emirovschi.sm.lab3.components.Metronom;
import com.emirovschi.sm.lab3.components.MultiEventHandler;
import com.emirovschi.sm.lab3.components.NoiseControl;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import net.beadsproject.beads.core.AudioContext;

public class AudioPlayerView extends GridPane
{
    public AudioPlayerView(final AudioContext audioContext, final MultiEventHandler onClose, final Window primaryStage)
    {
        final AdvancedSamplePlayer leftPlayer = new AdvancedSamplePlayer("Left player", audioContext, onClose, primaryStage);
        final AdvancedSamplePlayer rightPlayer = new AdvancedSamplePlayer("Right player", audioContext, onClose, primaryStage);
        final NoiseControl noiseControl = new NoiseControl(audioContext);
        final Metronom metronom = new Metronom(audioContext);

        add(leftPlayer, 0, 0);
        add(rightPlayer, 1, 0);
        add(noiseControl, 0, 1);
        add(metronom, 1, 1);

        GridPane.setVgrow(leftPlayer, Priority.ALWAYS);
        GridPane.setVgrow(rightPlayer, Priority.ALWAYS);

        setPadding(new Insets(10));
        setVgap(10);
        setHgap(10);
    }
}
