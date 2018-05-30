package com.emirovschi.sm.lab5.medias;

import java.io.IOException;

public interface ImageFacade
{
    byte[] getPreview(byte[] image, String imageType) throws IOException;

    byte[] generate(String text, int width, int height) throws IOException;
}
