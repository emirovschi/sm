package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ColorAdjustment extends Component<GridPane>
{
    private static final int MIN = -1;
    private static final int MAX = 1;
    private static final double STEP = 0.01;

    private final Slider contrast;
    private final Slider brightness;
    private final Slider saturation;
    private final Slider hue;
    private final Workspace workspace;

    public ColorAdjustment(final Workspace workspace)
    {
        super("Color adjustment", new GridPane());
        this.workspace = workspace;

        contrast = new Slider(MIN, MAX, STEP);
        brightness = new Slider(MIN, MAX, STEP);
        saturation = new Slider(MIN, MAX, STEP);
        hue = new Slider(MIN, MAX, STEP);

        contrast.valueProperty().addListener((observable, oldValue, newValue) -> workspace.getCurrentImage().getColorAdjust().setContrast((double) newValue));
        brightness.valueProperty().addListener((observable, oldValue, newValue) -> workspace.getCurrentImage().getColorAdjust().setBrightness((double) newValue));
        saturation.valueProperty().addListener((observable, oldValue, newValue) -> workspace.getCurrentImage().getColorAdjust().setSaturation((double) newValue));
        hue.valueProperty().addListener((observable, oldValue, newValue) -> workspace.getCurrentImage().getColorAdjust().setHue((double) newValue));

        final Button tintButton = new Button("Tint");
        tintButton.setMaxWidth(Double.MAX_VALUE);
        tintButton.setOnAction(event -> tint(workspace));

        final Button invertButton = new Button("Invert colors");
        invertButton.setMaxWidth(Double.MAX_VALUE);
        invertButton.setOnAction(event -> invertColors(workspace));

        GridPane.setHgrow(contrast, Priority.ALWAYS);
        GridPane.setHgrow(brightness, Priority.ALWAYS);
        GridPane.setHgrow(saturation, Priority.ALWAYS);
        GridPane.setHgrow(hue, Priority.ALWAYS);
        GridPane.setHgrow(tintButton, Priority.ALWAYS);

        getContent().add(new Label("Contrast"), 0, 0);
        getContent().add(new Label("Brightness"), 0, 1);
        getContent().add( new Label("Saturation"), 0, 2);
        getContent().add( new Label("HUE"), 0, 3);
        getContent().add(contrast, 1, 0);
        getContent().add(brightness, 1, 1);
        getContent().add(saturation, 1, 2);
        getContent().add(hue, 1, 3);
        getContent().add(tintButton, 0, 4, 2, 1);
        getContent().add(invertButton, 0, 5, 2, 1);
        getContent().setVgap(5);

        this.workspace.getImageObservable().addObserver((observer, value) -> updateSliders());
    }

    private void updateSliders()
    {
        if (workspace.getCurrentImage() != null)
        {
            contrast.setValue(workspace.getCurrentImage().getColorAdjust().getContrast());
            brightness.setValue(workspace.getCurrentImage().getColorAdjust().getBrightness());
            saturation.setValue(workspace.getCurrentImage().getColorAdjust().getSaturation());
            hue.setValue(workspace.getCurrentImage().getColorAdjust().getHue());
        }
    }

    private void invertColors(final Workspace workspace)
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage inverted = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        final WritableRaster originalRaster = original.getRaster();
        final WritableRaster invertedRaster = inverted.getRaster();

        int[] color = new int[4];
        for (int x = 0 ; x < originalRaster.getWidth(); x++)
        {
            for (int y = 0 ; y < originalRaster.getHeight(); y++)
            {
                int[] pixel = originalRaster.getPixel(x, y, color);
                pixel[0] = 255 - pixel[0];
                pixel[1] = 255 - pixel[1];
                pixel[2] = 255 - pixel[2];
                invertedRaster.setPixel(x, y, pixel);
            }
        }

        inverted.setData(invertedRaster);
        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(inverted, null));
    }

    private void tint(final Workspace workspace)
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage tinted = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        tinted.getGraphics().drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(tinted, null));
    }
}
