package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.io.File;
import java.util.List;
import java.util.Observable;

import static java.util.stream.Collectors.toList;
import static javafx.scene.layout.VBox.setVgrow;

public class Workspace extends Component<VBox>
{
    private final StackPane imageViewPane;
    private final ListView<ImageCellContainer> imagesView;
    private final ObservableList<ImageCellContainer> images;
    private final DoubleProperty imageViewWidth;
    private final DoubleProperty imageViewHeight;
    private final Observable imageObservable;

    private ImageCellContainer currentImage;

    public Workspace(final Window window)
    {
        super("Workspace", new VBox());
        imagesView = new ListView<>();
        images = FXCollections.observableArrayList();
        imageObservable = new Observable()
        {
            @Override
            public void notifyObservers()
            {
                setChanged();
                super.notifyObservers();
            }
        };

        imageViewWidth = new SimpleDoubleProperty(getContent().widthProperty().doubleValue());
        imageViewHeight = new SimpleDoubleProperty(getContent().heightProperty().doubleValue());
        getContent().widthProperty().addListener((observable, oldValue, newValue) -> setImageViewSize());
        window.heightProperty().addListener((observable, oldValue, newValue) -> setImageViewSize());

        imageViewPane = new StackPane();

        imagesView.setItems(images);
        imagesView.setMaxHeight(100);
        imagesView.setMinHeight(100);
        imagesView.setOrientation(Orientation.HORIZONTAL);
        imagesView.setCellFactory(new ImageCellFactory());
        imagesView.setFixedCellSize(85);
        imagesView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> setImage(newValue));

        setVgrow(imageViewPane, Priority.ALWAYS);
        setVgrow(imagesView, Priority.NEVER);

        getContent().setSpacing(20);
        getContent().setMinWidth(50);
        add(imageViewPane);
        add(imagesView);
    }

    public void setImageViewSize()
    {
        if (currentImage != null)
        {
            final double maxWidth = getContent().getWidth();
            final double maxHeight = getScene().getHeight() - 180;

            final double imageWidth = currentImage.getImageView().getImage().getWidth();
            final double imageHeight = currentImage.getImageView().getImage().getHeight();

            final double scaleWidth = maxWidth / imageWidth;
            final double scaleHeight = maxHeight / imageHeight;

            final double scale = Math.min(scaleWidth, scaleHeight);

            imageViewWidth.setValue(imageWidth * scale);
            imageViewHeight.setValue(imageHeight * scale);

            getContent().setAlignment(Pos.TOP_CENTER);
        }
    }

    private void setImage(final ImageCellContainer container)
    {
        imageViewPane.getChildren().clear();
        try
        {
            currentImage = container;
            imageViewPane.getChildren().add(currentImage.getImageView());
        }
        catch (final Exception exception)
        {
            currentImage = null;
        }
        setImageViewSize();
        imageObservable.notifyObservers();
    }

    public ImageCellContainer getCurrentImage()
    {
        return currentImage;
    }

    public void setImages(final List<File> images)
    {
        final List<ImageCellContainer> containers = images.stream()
                .map(image -> new ImageCellContainer(image, imageViewWidth, imageViewHeight, 75))
                .collect(toList());

        this.images.clear();
        this.images.addAll(containers);
        imagesView.getSelectionModel().select(0);
    }

    public Observable getImageObservable()
    {
        return imageObservable;
    }

    public void nextImage()
    {
        if (images.size() > 1)
        {
            final int currentIndex = images.indexOf(currentImage);
            final int newIndex = currentIndex == images.size() - 1 ? 0 : currentIndex + 1;
            imagesView.getSelectionModel().select(newIndex);
        }
    }
}
