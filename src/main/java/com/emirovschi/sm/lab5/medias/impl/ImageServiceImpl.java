package com.emirovschi.sm.lab5.medias.impl;

import com.emirovschi.sm.lab5.medias.ImageService;
import org.springframework.stereotype.Service;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

@Service
public class ImageServiceImpl implements ImageService
{
    private final static int PREVIEW_WIDTH = 600;

    @Override
    public BufferedImage getPreview(final BufferedImage image, final int type)
    {
        return resize(image, type, PREVIEW_WIDTH, image.getHeight() * PREVIEW_WIDTH / image.getWidth());
    }

    @Override
    public BufferedImage generate(final String text, final int width, final int height)
    {
        final BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        final int textHeight = height * 75 / 100;
        g.setFont(new Font("TimesRoman", Font.PLAIN, textHeight));

        final FontMetrics fontMetrics = g.getFontMetrics();
        final int textWidth = fontMetrics.stringWidth(text);

        g.setColor(new Color(225, 245, 254));
        g.fillRect(0, 0, 500, 500);
        g.setColor(new Color(2, 119, 189));
        g.drawString(text, (width - textWidth)/2, textHeight);

        g.dispose();
        return image;
    }

    private BufferedImage resize(final Image image, final int type, final int scaledWidth, final int scaledHeight)
    {
        final BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, type);
        final Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return resizedImage;
    }
}
