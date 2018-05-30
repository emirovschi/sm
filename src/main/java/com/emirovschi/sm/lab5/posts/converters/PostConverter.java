package com.emirovschi.sm.lab5.posts.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.converters.Populator;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("postConverter")
public class PostConverter implements Converter<PostModel, PostDTO>
{
    @Resource
    private Populator<PostModel, PostDTO> postMinimalPopulator;

    @Resource
    private Populator<PostModel, PostDTO> postVotesPopulator;

    @Override
    public PostDTO convert(final PostModel post)
    {
        final PostDTO postDTO = new PostDTO();
        postMinimalPopulator.populate(post, postDTO);
        postVotesPopulator.populate(post, postDTO);
        return postDTO;
    }
}
