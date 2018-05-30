package com.emirovschi.sm.lab3;

import com.emirovschi.sm.lab3.components.MultiEventHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.beadsproject.beads.core.AudioContext;

public class AudioPlayerApplication extends Application
{
    private static AudioContext AUDIO_CONTEXT;

    @Override
    public void start(final Stage primaryStage)
    {
        final MultiEventHandler onClose = new MultiEventHandler();
        primaryStage.setOnCloseRequest(event -> onClose.trigger());

        final AudioPlayerView audioPlayerView = new AudioPlayerView(AUDIO_CONTEXT, onClose, primaryStage);
        final Scene scene = new Scene(audioPlayerView, 800, 600);

        AUDIO_CONTEXT.start();

        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Media Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args)
    {
        try
        {
            AUDIO_CONTEXT = new AudioContext();
            launch(args);
        }
        finally
        {
            AUDIO_CONTEXT.stop();
        }
    }
}
