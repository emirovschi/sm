package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RotationControl extends Component<GridPane>
{
    public RotationControl(final Workspace workspace)
    {
        super("Rotation", new GridPane());

        final Button flipVertical = new Button("Flip vertical");
        final Button flipHorizontal = new Button("Flip horizontal");
        final Button rotateCCW = new Button("Rotate CWW");
        final Button rotateCW = new Button("Rotate CW");

        flipVertical.setOnAction(event -> flipVertical(workspace));
        flipHorizontal.setOnAction(event -> flipHorizontal(workspace));
        rotateCCW.setOnAction(event -> rotateCCW(workspace));
        rotateCW.setOnAction(event -> rotateCW(workspace));

        flipVertical.setMaxWidth(Double.MAX_VALUE);
        flipHorizontal.setMaxWidth(Double.MAX_VALUE);
        rotateCCW.setMaxWidth(Double.MAX_VALUE);
        rotateCW.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(flipVertical, Priority.ALWAYS);
        GridPane.setHgrow(flipHorizontal, Priority.ALWAYS);
        GridPane.setHgrow(rotateCCW, Priority.ALWAYS);
        GridPane.setHgrow(rotateCW, Priority.ALWAYS);

        getContent().add(flipVertical, 0, 0);
        getContent().add(flipHorizontal, 1, 0);
        getContent().add(rotateCCW, 0, 1);
        getContent().add(rotateCW, 1, 1);
        getContent().setHgap(10);
        getContent().setVgap(5);
    }

    private void flipVertical(final Workspace workspace)
    {
        redraw(workspace,
                original -> new BufferedImage(original.getWidth(), original.getHeight(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(original,
                        0, 0, original.getWidth(), original.getHeight(),
                        original.getWidth(), 0, 0, original.getHeight(),
                        null));
    }

    private void flipHorizontal(final Workspace workspace)
    {
        redraw(workspace,
                original -> new BufferedImage(original.getWidth(), original.getHeight(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(original,
                0, 0, original.getWidth(), original.getHeight(),
                0, original.getHeight(), original.getWidth(), 0,
                null));
    }

    private void rotateCCW(final Workspace workspace)
    {
        final AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(workspace.getCurrentImage().getImage().getHeight() / 2, workspace.getCurrentImage().getImage().getWidth() / 2);
        affineTransform.rotate(-Math.PI/2);
        affineTransform.translate(-workspace.getCurrentImage().getImage().getWidth() / 2, -workspace.getCurrentImage().getImage().getHeight() / 2);
        final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        redraw(workspace,
                original -> new BufferedImage(original.getHeight(), original.getWidth(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(affineTransformOp.filter(original, null),
                        0, 0, newImage.getWidth(), newImage.getHeight(),
                        null));
    }

    private void rotateCW(final Workspace workspace)
    {
        final AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(workspace.getCurrentImage().getImage().getHeight() / 2, workspace.getCurrentImage().getImage().getWidth() / 2);
        affineTransform.rotate(Math.PI/2);
        affineTransform.translate(-workspace.getCurrentImage().getImage().getWidth() / 2, -workspace.getCurrentImage().getImage().getHeight() / 2);
        final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        redraw(workspace,
                original -> new BufferedImage(original.getHeight(), original.getWidth(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(affineTransformOp.filter(original, null),
                        0, 0, newImage.getWidth(), newImage.getHeight(),
                        null));
    }

    private void redraw(
            final Workspace workspace,
            final Function<BufferedImage, BufferedImage> copy,
            final BiConsumer<BufferedImage, BufferedImage> transform)
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage flipped = copy.apply(original);
        transform.accept(original, flipped);
        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(flipped, null));
        workspace.setImageViewSize();
    }
}
