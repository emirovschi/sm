package com.emirovschi.sm.lab5.posts.converters;

import com.emirovschi.sm.lab5.converters.Populator;
import com.emirovschi.sm.lab5.exceptions.NotFoundException;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import com.emirovschi.sm.lab5.users.UserService;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

@Component("postVotesPopulator")
public class PostVotesPopulator implements Populator<PostModel, PostDTO>
{
    @Autowired
    private UserService userService;

    @Override
    public void populate(final PostModel source, final PostDTO target)
    {
        target.setId(source.getId());
        target.setUps(getUps(source));
        target.setDowns(getDowns(source));
        target.setUserVote(getUserVote(source));
    }

    private int getUps(final PostModel post)
{
    return getVotes(post).filter(i -> i > 0).sum();
}

    private int getDowns(final PostModel post)
    {
        return -getVotes(post).filter(i -> i < 0).sum();
    }

    private IntStream getVotes(final PostModel post)
    {
        return getVotesMap(post).values().stream().mapToInt(i -> i);
    }

    private int getUserVote(final PostModel post)
    {
        try
        {
            return ofNullable(getVotesMap(post).get(userService.getSessionUser())).orElse(0);
        }
        catch (NotFoundException exception)
        {
            return 0;
        }
    }

    private Map<UserModel, Integer> getVotesMap(final PostModel post)
    {
        return ofNullable(post.getVotes()).orElse(emptyMap());
    }
}
