package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.EventProcessor;
import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;

import java.io.File;
import java.util.Observable;

import static java.util.Optional.ofNullable;

public class FileManager extends GridPane
{
    private final SamplePlayer player;
    private final Window window;
    private final Label sampleName;
    private final Label sampleFileName;
    private final Observable onSampleOpen;

    public FileManager(final UGenChain chain, final Window window)
    {
        this.player = new GranularSamplePlayer(chain.getAudioContext(), 2);
        this.player.setKillOnEnd(false);
        this.player.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
        this.player.setEndListener(new EventProcessor(player::reset));

        this.window = window;
        this.sampleName = new Label(" ");
        this.sampleName.setWrapText(true);
        this.sampleFileName = new Label(" ");
        this.sampleFileName.setWrapText(true);
        this.onSampleOpen = new Observable()
        {
            @Override
            public void notifyObservers()
            {
                setChanged();
                super.notifyObservers();
            }
        };

        chain.addUGen(this.player);

        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Songs", "*.mp3");
        final FileChooser openFileChooser = createFileChooser(filter);

        final Button open = new Button("Open");
        open.setOnAction(event -> open(openFileChooser));
        open.setMaxWidth(Double.MAX_VALUE);
        open.setMaxHeight(Double.MAX_VALUE);
        open.setMinWidth(50);

        GridPane.setVgrow(open, Priority.ALWAYS);

        setHgap(10);
        setVgap(10);

        add(open, 0, 0, 1, 2);
        add(sampleName, 1, 0, 1, 1);
        add(sampleFileName, 1, 1, 1, 1);
    }

    private FileChooser createFileChooser(final FileChooser.ExtensionFilter filter)
    {
        final FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Open song");
        saveFileChooser.getExtensionFilters().add(filter);
        return saveFileChooser;
    }

    private void open(final FileChooser openFileChooser)
    {
        ofNullable(openFileChooser.showOpenDialog(window))
                .ifPresent(this::openSong);
    }

    private void openSong(final File file)
    {
        player.setSample(SampleManager.sample(file.getAbsolutePath()));
        player.reset();
        changeSong();
        onSampleOpen.notifyObservers();
    }

    private void changeSong()
    {
        final String fileName = new File(player.getSample().getFileName()).getName();
        int pos = fileName.lastIndexOf(".");
        final String name = pos > 0 ? fileName.substring(0, pos) : fileName;

        sampleName.setText(name);
        sampleFileName.setText(player.getSample().getFileName());
    }

    public SamplePlayer getPlayer()
    {
        return player;
    }

    public void onSampleChange(final Runnable runnable)
    {
        onSampleOpen.addObserver((observable, value) -> runnable.run());
    }
}
