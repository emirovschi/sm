package com.emirovschi.sm.lab5.medias;

import com.emirovschi.sm.lab5.posts.models.PostModel;

public interface MediaFacade
{
    int getWidth(PostModel post);

    int getHeight(PostModel post);
}
