package com.emirovschi.sm.lab5.medias;

import java.awt.image.BufferedImage;

public interface ImageService
{
    BufferedImage getPreview(final BufferedImage image, final int type);

    BufferedImage generate(String text, int width, int height);
}
