package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class Equalizer extends HBox
{
    private final List<Band> bands;

    public Equalizer(final FileManager fileManager, final UGenChain chain)
    {
        bands = new ArrayList<>();
        bands.add(new Band(chain, 32f, 19f));
        bands.add(new Band(chain, 64f, 39f));
        bands.add(new Band(chain, 125f, 78f));
        bands.add(new Band(chain, 250f, 156f));
        bands.add(new Band(chain, 500f, 312f));
        bands.add(new Band(chain, 1000f, 625f));
        bands.add(new Band(chain, 2000f, 1250f));
        bands.add(new Band(chain, 4000f, 2500f));
        bands.add(new Band(chain, 8000f, 5000f));
        bands.add(new Band(chain, 16000f, 10000f));

        bands.forEach(this::add);

        setSpacing(10);
        setMaxWidth(Double.MAX_VALUE);

        setMinHeight(50);
        setPrefHeight(50);
        setMaxHeight(Double.MAX_VALUE);
        setDisable(true);

        fileManager.onSampleChange(this::enable);
    }

    private void enable()
    {
        setDisable(false);
    }

    private void add(final Band band)
    {
        final StackPane pane = new StackPane();
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.getChildren().add(band);
        getChildren().add(pane);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }
}
