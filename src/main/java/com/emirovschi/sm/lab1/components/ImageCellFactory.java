package com.emirovschi.sm.lab1.components;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ImageCellFactory implements Callback<ListView<ImageCellContainer>, ListCell<ImageCellContainer>>
{
    @Override
    public ListCell<ImageCellContainer> call(final ListView<ImageCellContainer> param)
    {
        return new ImageCell();
    }
}
