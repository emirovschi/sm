package com.emirovschi.sm.lab5.posts.impl;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.medias.ImageFacade;
import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.list.dto.PageDTO;
import com.emirovschi.sm.lab5.posts.PostFacade;
import com.emirovschi.sm.lab5.posts.PostService;
import com.emirovschi.sm.lab5.posts.dto.CommentDTO;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.posts.exceptions.BadMediaException;
import com.emirovschi.sm.lab5.posts.models.PostModel;
import com.emirovschi.sm.lab5.search.Search;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import com.emirovschi.sm.lab5.tags.TagService;
import com.emirovschi.sm.lab5.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostFacadeImpl implements PostFacade
{
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageFacade imageFacade;

    @Resource
    private Converter<PostModel, PostDTO> postConverter;

    @Autowired
    private Converter<SearchDTO, Search> searchReverseConverter;

    @Resource
    private Converter<Page<PostModel>, PageDTO<PostDTO>> postPageConverter;

    @Resource
    private Converter<Page<PostModel>, PageDTO<PostDTO>> postVotesPageConverter;

    @Resource
    private Converter<PostModel, MediaDTO> mediaConverter;

    @Resource
    private Converter<PostModel, MediaDTO> previewConverter;

    @Override
    public PageDTO<PostDTO> search(final SearchDTO searchDTO, final Pageable pageable)
    {
        return search(searchDTO, pageable, postPageConverter);
    }

    @Override
    public PageDTO<PostDTO> getVotes(final SearchDTO searchDTO, final Pageable pageable)
    {
        return search(searchDTO, pageable, postVotesPageConverter);
    }

    private PageDTO<PostDTO> search(final SearchDTO searchDTO, final Pageable pageable,
                                   final Converter<Page<PostModel>, PageDTO<PostDTO>> converter)
    {
        final Search search = searchReverseConverter.convert(searchDTO);
        final Page<PostModel> posts = postService.search(search.getAdds(), search.getExcludes(), search.getFirstId(), pageable);
        return converter.convert(posts);
    }

    @Override
    public PostDTO getPost(final long id)
    {
        return postConverter.convert(postService.getPostById(id));
    }

    @Override
    public MediaDTO getPostMedia(final long id)
    {
        return mediaConverter.convert(postService.getPostById(id));
    }

    @Override
    public MediaDTO getPostPreview(final long id)
    {
        return previewConverter.convert(postService.getPostById(id));
    }

    @Override
    public void voteUp(final long id)
    {
        vote(id, 1);
    }

    @Override
    public void voteDown(final long id)
    {
        vote(id, -1);
    }

    private void vote(final long id, final int vote)
    {
        final PostModel post = postService.getPostById(id);
        postService.vote(post, userService.getSessionUser(), vote);
    }

    @Override
    public void comment(final long id, final CommentDTO comment)
    {

    }

    @Override
    @Transactional
    public PostDTO create(final String title, final List<String> tags, final String mediaType, final byte[] media)
    {
        try
        {
            final PostModel post = new PostModel();
            post.setTitle(title);
            post.setTags(tagService.save(tags.stream().distinct().collect(Collectors.toList())));
            post.setMediaType(mediaType);
            post.setMedia(media);
            post.setPreview(createPreview(mediaType, media));
            post.setUser(userService.getSessionUser());
            postService.save(post);
            return postConverter.convert(post);
        }
        catch (IOException e)
        {
            throw new BadMediaException();
        }
    }

    private byte[] createPreview(final String mediaType, final byte[] media) throws IOException
    {
        switch (mediaType.substring(0, mediaType.indexOf('/')).toLowerCase())
        {
            case "video":
            case "audio":
                return media;
            default:
                return imageFacade.getPreview(media, mediaType);
        }
    }

    @Override
    public void delete(final long id)
    {
        postService.delete(postService.getPostById(id));
    }
}
