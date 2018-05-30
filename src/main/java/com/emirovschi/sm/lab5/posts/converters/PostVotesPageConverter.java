package com.emirovschi.sm.lab5.posts.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.list.converters.PageConverter;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("postVotesPageConverter")
public class PostVotesPageConverter extends PageConverter<PostModel, PostDTO>
{
    @Resource
    private Converter<PostModel, PostDTO> postVotesConverter;

    @Override
    protected Converter<PostModel, PostDTO> getItemConverter()
    {
        return postVotesConverter;
    }
}
