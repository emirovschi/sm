package com.emirovschi.sm.lab3.components;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.concurrent.atomic.AtomicBoolean;

public class Controls extends HBox
{
    private final FileManager fileManager;
    private final Button playButton;
    private final Slider timeSlider;
    private final Label timeLabel;
    private final AtomicBoolean running;
    private final Thread timeThread;

    public Controls(FileManager fileManager, final MultiEventHandler onClose)
    {
        this.fileManager = fileManager;

        running = new AtomicBoolean(true);
        timeThread = new Thread(this::updateTimer);

        playButton = new Button("Play");
        timeSlider = new Slider(0, 1, 0);
        timeLabel = new Label("-/-");

        playButton.setPrefWidth(50);
        playButton.setOnAction(event -> togglePlayButton());

        timeSlider.valueProperty().addListener(((observable, oldValue, newValue) -> setPosition()));

        HBox.setHgrow(playButton, Priority.NEVER);
        HBox.setHgrow(timeSlider, Priority.ALWAYS);

        getChildren().addAll(playButton, timeSlider, timeLabel);

        setSpacing(10);
        setAlignment(Pos.CENTER);
        setDisable(true);

        fileManager.onSampleChange(this::enable);
        onClose.onEvent(() -> running.set(false));

        timeThread.start();
    }

    private void enable()
    {
        setDisable(false);
        playButton.setText("Pause");
        updateTimerValues();
    }

    private void togglePlayButton()
    {
        if (fileManager.getPlayer().isPaused())
        {
            if (Math.abs(fileManager.getPlayer().getPosition() - fileManager.getPlayer().getSample().getLength()) < 0.001)
            {
                fileManager.getPlayer().reset();
            }
            else
            {
                fileManager.getPlayer().start();
            }
            playButton.setText("Pause");
        }
        else
        {
            fileManager.getPlayer().pause(true);
            playButton.setText("Play");
        }
    }

    private void updateTimer()
    {
        while (running.get())
        {
            try
            {
                Platform.runLater(this::updateTimerValues);
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void updateTimerValues()
    {
        if (fileManager.getPlayer().getSample() == null)
        {
            timeSlider.setValue(0);
            timeLabel.setText("-/-");
        }
        else
        {
            final double currentTime = fileManager.getPlayer().getPosition();
            final double totalTime = fileManager.getPlayer().getSample().getLength();
            timeLabel.setText(formatTime(currentTime) + "/" + formatTime(totalTime));
            timeSlider.setValue(currentTime/totalTime);
        }
    }

    private String formatTime(final double time)
    {
        int seconds = (int) (time / 1000);
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private void setPosition()
    {
        final double totalTime = fileManager.getPlayer().getSample().getLength();
        final double currentTime = timeSlider.getValue() * totalTime;
        fileManager.getPlayer().setPosition(currentTime);
    }
}
