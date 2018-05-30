package com.emirovschi.sm.lab5.posts;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.exceptions.dto.ErrorDTO;
import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.list.dto.PageDTO;
import com.emirovschi.sm.lab5.posts.dto.PostDTO;
import com.emirovschi.sm.lab5.posts.exceptions.BadMediaException;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController
{
    @Autowired
    private PostFacade postFacade;

    @Resource
    private Set<String> allowedContentTypes;

    @Autowired
    private Converter<Exception, ErrorDTO> errorConverter;

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.POST)
    public PostDTO create(@RequestParam final String title, @RequestParam final List<String> tags,
                          @RequestParam final MultipartFile media) throws IOException, BadMediaException
    {
        if (!allowedContentTypes.contains(media.getContentType()))
        {
            throw new BadMediaException();
        }

        return postFacade.create(title, tags, media.getContentType(), media.getBytes());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public PageDTO<PostDTO> searchPosts(@RequestBody final SearchDTO search, @PageableDefault final Pageable pageable)
    {
        return postFacade.search(search, pageable);
    }

    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    public PageDTO<PostDTO> getPostsVotes(@RequestBody final SearchDTO search, @PageableDefault final Pageable pageable)
    {
        return postFacade.getVotes(search, pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PostDTO getPost(@PathVariable final long id)
    {
        return postFacade.getPost(id);
    }

    @RequestMapping(value = "/{id}/media", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "video/mp4", "audio/mp3"})
    public ResponseEntity<byte[]> getPostMedia(@PathVariable final long id)
    {
        final MediaDTO media = postFacade.getPostMedia(id);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(media.getType());

        return new ResponseEntity<>(media.getMedia(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "video/mp4", "audio/mp3"})
    public ResponseEntity<byte[]> getPostPreview(@PathVariable final long id)
    {
        final MediaDTO image = postFacade.getPostPreview(id);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(image.getType());

        return new ResponseEntity<>(image.getMedia(), responseHeaders, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/{id}/vote/up", method = RequestMethod.POST)
    public void voteUp(@PathVariable final long id)
    {
        postFacade.voteUp(id);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/{id}/vote/down", method = RequestMethod.POST)
    public void voteDown(@PathVariable final long id)
    {
        postFacade.voteDown(id);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadMediaException.class)
    public ErrorDTO handleBadMediaException(BadMediaException exception)
    {
        return errorConverter.convert(exception);
    }
}
