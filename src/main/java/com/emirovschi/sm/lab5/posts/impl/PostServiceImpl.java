package com.emirovschi.sm.lab5.posts.impl;

import com.emirovschi.sm.lab5.exceptions.NotFoundException;
import com.emirovschi.sm.lab5.posts.CommentRepository;
import com.emirovschi.sm.lab5.posts.PostRepository;
import com.emirovschi.sm.lab5.posts.PostService;
import com.emirovschi.sm.lab5.posts.models.CommentModel;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Service
public class PostServiceImpl implements PostService
{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<PostModel> search(final Set<TagModel> adds, final Set<TagModel> excludes, final Long firstId, final Pageable pageable)
    {
        return postRepository.search(ofNullable(adds).map(Collection::size).orElse(0), adds, excludes, firstId, pageable);
    }

    @Override
    public PostModel getPostById(final long id)
    {
        return ofNullable(postRepository.findById(id)).orElseThrow(NotFoundException::new);
    }

    @Override
    public void vote(final PostModel post, final UserModel user, final int value)
    {
        final Map<UserModel, Integer> votes = ofNullable(post.getVotes()).orElseGet(() -> buildVoteMap(post));
        votes.put(user, value);
        postRepository.save(post);
    }

    private Map<UserModel, Integer> buildVoteMap(final PostModel post)
    {
        post.setVotes(new HashMap<>());
        return post.getVotes();
    }

    @Override
    public void addComment(final PostModel post, final CommentModel comment)
    {
        final List<CommentModel> comments = ofNullable(post.getComments()).orElseGet(() -> buildCommentList(post));
        comments.add(comment);
        postRepository.save(post);
    }

    private List<CommentModel> buildCommentList(final PostModel post)
    {
        post.setComments(new ArrayList<>());
        return post.getComments();
    }

    @Override
    public void save(final PostModel post)
    {
        postRepository.save(post);
    }

    @Override
    public void delete(final PostModel post)
    {
        postRepository.delete(post);
    }
}
