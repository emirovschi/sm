package com.emirovschi.sm.lab5.medias.dto;

import org.springframework.http.MediaType;

public class MediaDTO
{
    private byte[] media;
    private MediaType type;

    public byte[] getMedia()
    {
        return media;
    }

    public void setMedia(final byte[] media)
    {
        this.media = media;
    }

    public MediaType getType()
    {
        return type;
    }

    public void setType(final MediaType type)
    {
        this.type = type;
    }
}
