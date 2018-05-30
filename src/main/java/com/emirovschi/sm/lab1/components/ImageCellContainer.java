package com.emirovschi.sm.lab1.components;

import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageCellContainer
{
    private final ImageView imageView;
    private final ImageView thumbnail;
    private ColorAdjust colorAdjust;
    private boolean draw;
    private Point2D drawPosition;
    private double drawScale;
    private BufferedImage canvas;
    private Graphics2D graphics;

    public ImageCellContainer(final File file, final DoubleProperty width, final DoubleProperty height, final int thumbnailSize)
    {
        colorAdjust = new ColorAdjust();

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(width);
        imageView.fitHeightProperty().bind(height);
        imageView.setEffect(colorAdjust);
        imageView.setCursor(Cursor.CROSSHAIR);
        imageView.setOnMousePressed(event -> toggleDraw(event, true));
        imageView.setOnMouseDragged(this::draw);
        imageView.setOnMouseReleased(event -> toggleDraw(event, false));

        thumbnail = new ImageView();
        thumbnail.setPreserveRatio(true);
        thumbnail.setFitHeight(thumbnailSize);
        thumbnail.setFitWidth(thumbnailSize);
        thumbnail.setEffect(colorAdjust);

        setImage(getImage(file));
    }

    private void toggleDraw(final MouseEvent event, final boolean value)
    {
        if (event.getButton().equals(MouseButton.PRIMARY))
        {
            draw = value;

            if (draw)
            {
                drawScale = getImage().getWidth() / getImageView().getFitWidth();
                drawPosition = new Point2D(event.getX() * drawScale, event.getY() * drawScale);
                final BufferedImage original = SwingFXUtils.fromFXImage(getImage(), null);
                canvas = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
                graphics = (Graphics2D) canvas.getGraphics();
                graphics.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
                graphics.setStroke(new BasicStroke(10));
                graphics.setColor(Color.RED);
            }
        }
    }

    private void draw(final MouseEvent event)
    {
        if (draw)
        {
            final Point2D newDrawPosition = new Point2D(event.getX() * drawScale, event.getY() * drawScale);
            graphics.drawLine(convert(drawPosition.getX()), convert(drawPosition.getY()), convert(newDrawPosition.getX()), convert(newDrawPosition.getY()));
            setImage(SwingFXUtils.toFXImage(canvas, null));
            drawPosition = newDrawPosition;
        }
    }

    private int convert(final Double value)
    {
        return value.intValue();
    }

    private Image getImage(final File file)
    {
        try
        {
            return new Image(new FileInputStream(file));
        }
        catch (final IOException exception)
        {
            return null;
        }
    }

    public Image getImage()
    {
        return imageView.getImage();
    }

    public void setImage(final Image image)
    {
        imageView.setImage(image);
        thumbnail.setImage(image);
    }

    public ImageView getImageView()
    {
        return imageView;
    }

    public ImageView getThumbnail()
    {
        return thumbnail;
    }

    public ColorAdjust getColorAdjust()
    {
        return colorAdjust;
    }
}
