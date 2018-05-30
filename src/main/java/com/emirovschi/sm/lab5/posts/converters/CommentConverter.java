package com.emirovschi.sm.lab5.posts.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.posts.dto.CommentDTO;
import com.emirovschi.sm.lab5.posts.models.CommentModel;
import com.emirovschi.sm.lab5.users.dto.UserDTO;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter implements Converter<CommentModel, CommentDTO>
{
    @Autowired
    private Converter<UserModel, UserDTO> userConverter;

    @Override
    public CommentDTO convert(final CommentModel comment)
    {
        final CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUser(userConverter.convert(comment.getUser()));
        commentDTO.setComment(comment.getComment());
        commentDTO.setTime(comment.getTime());
        return commentDTO;
    }
}
