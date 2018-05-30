package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;
import static javafx.scene.layout.HBox.setHgrow;

public class FileManager extends Component<HBox>
{
    private final Workspace workspace;
    private final Window window;

    public FileManager(final Workspace workspace, final Window window)
    {
        super("File", new HBox());
        this.workspace = workspace;
        this.window = window;

        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
        final FileChooser openFileChooser = createFileChooser("Open images", filter);
        final FileChooser saveFileChooser = createFileChooser("Save image", filter);

        final Button open = new Button("Open");
        open.setOnAction(event -> open(openFileChooser));
        open.setMaxWidth(Double.MAX_VALUE);

        final Button save = new Button("Save");
        save.setOnAction(event -> save(saveFileChooser));
        save.setMaxWidth(Double.MAX_VALUE);
        save.setDisable(true);

        setHgrow(open, Priority.ALWAYS);
        setHgrow(save, Priority.ALWAYS);

        getContent().setSpacing(10);

        add(open);
        add(save);

        workspace.getImageObservable().addObserver((observable, value) -> save.setDisable(workspace.getCurrentImage() == null));
    }

    private FileChooser createFileChooser(final String title, final FileChooser.ExtensionFilter filter)
    {
        final FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle(title);
        saveFileChooser.getExtensionFilters().add(filter);
        return saveFileChooser;
    }

    private void open(final FileChooser openFileChooser)
    {
        ofNullable(openFileChooser.showOpenMultipleDialog(window))
                .filter(list -> !list.isEmpty())
                .ifPresent(this::openImages);
    }

    private void openImages(final List<File> images)
    {
        workspace.setImages(images);
    }

    private void save(final FileChooser saveFileChooser)
    {
        ofNullable(saveFileChooser.showSaveDialog(window))
                .ifPresent(this::saveImage);
    }

    private void saveImage(final File location)
    {
        final String extension = FilenameUtils.getExtension(location.getName());
        final ImageView imageView = workspace.getCurrentImage().getImageView();

        final SnapshotParameters spa = new SnapshotParameters();
        final double scale = imageView.getImage().getWidth() / imageView.getFitWidth();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        final BufferedImage image = SwingFXUtils.fromFXImage(imageView.snapshot(spa, null), null);
        final BufferedImage fixedImage = extension.equalsIgnoreCase("png") ? image : fixImage(image);

        try
        {
            ImageIO.write(fixedImage, extension, location);
        }
        catch (final IOException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    private BufferedImage fixImage(final BufferedImage image)
    {
        final BufferedImage fixedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE);
        final Graphics2D graphics = fixedImage.createGraphics();
        graphics.drawImage(image,0, 0, null);
        return fixedImage;
    }
}
