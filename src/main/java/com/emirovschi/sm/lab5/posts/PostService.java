package com.emirovschi.sm.lab5.posts;

import com.emirovschi.sm.lab5.posts.models.CommentModel;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PostService
{
    Page<PostModel> search(Set<TagModel> adds, Set<TagModel> excludes, Long firstId, Pageable pageable);

    PostModel getPostById(long id);

    void vote(PostModel post, UserModel user, int value);

    void addComment(PostModel post, CommentModel comment);

    void save(PostModel post);

    void delete(PostModel post);
}
