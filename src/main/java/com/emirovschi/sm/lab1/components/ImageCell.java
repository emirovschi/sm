package com.emirovschi.sm.lab1.components;

import javafx.scene.control.ListCell;

import java.util.Optional;

public class ImageCell extends ListCell<ImageCellContainer>
{
    @Override
    protected void updateItem(final ImageCellContainer item, final boolean empty)
    {
        super.updateItem(item, empty);
        setText(null);
        Optional.ofNullable(item)
                .filter(file -> !empty)
                .map(ImageCellContainer::getThumbnail)
                .ifPresent(this::setGraphic);
    }
}
