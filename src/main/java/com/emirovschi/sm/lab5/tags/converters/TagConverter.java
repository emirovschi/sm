package com.emirovschi.sm.lab5.tags.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.posts.PostService;
import com.emirovschi.sm.lab5.tags.dto.TagDTO;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements Converter<TagModel, TagDTO>
{
    @Autowired
    private PostService postService;

    @Override
    public TagDTO convert(final TagModel tag)
    {
        final TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}
