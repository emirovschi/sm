package com.emirovschi.sm.lab5.posts;

import com.emirovschi.sm.lab5.posts.models.CommentModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<CommentModel, Long>
{
}
