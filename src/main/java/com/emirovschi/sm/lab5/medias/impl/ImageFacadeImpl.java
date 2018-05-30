package com.emirovschi.sm.lab5.medias.impl;

import com.emirovschi.sm.lab5.medias.ImageFacade;
import com.emirovschi.sm.lab5.medias.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static javax.imageio.ImageIO.read;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Component
public class ImageFacadeImpl implements ImageFacade
{
    @Autowired
    private ImageService imageService;

    @Override
    public byte[] getPreview(final byte[] image, final String imageType) throws IOException
    {
        final BufferedImage original = read(new ByteArrayInputStream(image));
        final BufferedImage resized = imageService.getPreview(original, getType(imageType));

        return convert(resized, imageType);
    }

    @Override
    public byte[] generate(final String text, final int width, final int height) throws IOException
    {
        return convert(imageService.generate(text, width, height), IMAGE_JPEG_VALUE);
    }

    private byte[] convert(final BufferedImage image, final String imageType) throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(37628);
        final ImageOutputStream ios =  ImageIO.createImageOutputStream(baos);

        final ImageWriter writer = ImageIO.getImageWritersByFormatName(getFormat(imageType)).next();
        final ImageWriteParam param = writer.getDefaultWriteParam();

        try
        {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1.0F);
        }
        catch (UnsupportedOperationException exception)
        {
            //
        }

        writer.setOutput(ios);
        writer.write(null,new IIOImage(image, null, null), param);

        return baos.toByteArray();
    }

    private int getType(final String imageType)
    {
        return imageType.equals(IMAGE_JPEG_VALUE) ? TYPE_INT_RGB : TYPE_INT_ARGB;
    }

    private String getFormat(final String imageType)
    {
        return imageType.replace("image/", "");
    }
}
