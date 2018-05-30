package com.emirovschi.sm.lab5.posts;

import com.emirovschi.sm.lab5.list.dto.PageDTO;
import com.emirovschi.sm.lab5.posts.dto.CommentDTO;
import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostFacade
{
    PageDTO<PostDTO> search(SearchDTO search, Pageable pageable);

    PageDTO<PostDTO> getVotes(SearchDTO search, Pageable pageable);

    PostDTO getPost(long id);

    MediaDTO getPostMedia(long id);

    MediaDTO getPostPreview(long id);

    void voteUp(long id);

    void voteDown(long id);

    void comment(long id, CommentDTO comment);

    PostDTO create(String title, List<String> tags, String mediaType, byte[] media);

    void delete(long id);
}
