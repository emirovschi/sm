package com.emirovschi.sm.lab5.medias.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.posts.exceptions.BadMediaException;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import org.h2.util.IOUtils;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class AbstractPostImageConverter implements Converter<PostModel, MediaDTO>
{
    @Override
    public MediaDTO convert(final PostModel post)
    {
        final MediaDTO mediaDTO = new MediaDTO();

        try
        {
            mediaDTO.setMedia(IOUtils.readBytesAndClose(new ByteArrayInputStream(getMedia(post)), -1));
        }
        catch (IOException e)
        {
            throw new BadMediaException();
        }

        mediaDTO.setType(MediaType.parseMediaType(post.getMediaType()));

        return mediaDTO;
    }

    protected abstract byte[] getMedia(PostModel post);
}
