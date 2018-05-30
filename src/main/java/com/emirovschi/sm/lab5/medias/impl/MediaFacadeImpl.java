package com.emirovschi.sm.lab5.medias.impl;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.emirovschi.sm.lab5.medias.MediaFacade;
import com.emirovschi.sm.lab5.posts.exceptions.BadMediaException;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

@Component("mediaFacaade")
public class MediaFacadeImpl implements MediaFacade
{
    @Override
    public int getWidth(final PostModel post)
    {
        if (post.getMediaType().startsWith("image"))
        {
            return getImage(post).getWidth();
        }
        else if (post.getMediaType().startsWith("video"))
        {
            return (int) getVideoData(post, TrackHeaderBox::getWidth)
                    .mapToLong(Math::round)
                    .max()
                    .orElseThrow(BadMediaException::new);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getHeight(final PostModel post)
    {
        if (post.getMediaType().startsWith("image"))
        {
            return getImage(post).getHeight();
        }
        else if (post.getMediaType().startsWith("video"))
        {
            return (int) getVideoData(post, TrackHeaderBox::getHeight)
                    .mapToLong(Math::round)
                    .max()
                    .orElseThrow(BadMediaException::new);
        }
        else
        {
            return 0;
        }
    }

    private BufferedImage getImage(final PostModel post)
    {
        try
        {
            return ImageIO.read(new ByteArrayInputStream(post.getPreview()));
        }
        catch (IOException e)
        {
            throw new BadMediaException();
        }
    }

    private <T> Stream<T> getVideoData(final PostModel post, final Function<TrackHeaderBox, T> function)
    {
        try
        {
            return new IsoFile(new MemoryDataSourceImpl(post.getMedia())).getMovieBox().getBoxes().stream()
                    .filter(TrackBox.class::isInstance)
                    .map(TrackBox.class::cast)
                    .map(TrackBox::getTrackHeaderBox)
                    .map(function);
        }
        catch (final IOException exception)
        {
            throw new BadMediaException();
        }
    }
}
