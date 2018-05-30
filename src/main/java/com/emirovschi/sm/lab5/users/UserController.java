package com.emirovschi.sm.lab5.users;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.exceptions.NotFoundException;
import com.emirovschi.sm.lab5.exceptions.dto.ErrorDTO;
import com.emirovschi.sm.lab5.medias.dto.MediaDTO;
import com.emirovschi.sm.lab5.users.dto.UpdateUserDTO;
import com.emirovschi.sm.lab5.users.dto.UserDTO;
import com.emirovschi.sm.lab5.users.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private Converter<Exception, ErrorDTO> errorConverter;

    @RequestMapping(method = GET)
    @Secured("ROLE_USER")
    public UserDTO get() throws NotFoundException
    {
        return userFacade.getSessionUser();
    }

    @RequestMapping(value = "/{id}/avatar", method = GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAvatar(@PathVariable final long id) throws NotFoundException
    {
        final MediaDTO image = userFacade.getAvatar(id);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(image.getType());

        return new ResponseEntity<>(image.getMedia(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = POST)
    public void register(@Validated @RequestBody final UserDTO user) throws UserAlreadyExistsException
    {
        userFacade.register(user);
    }

    @RequestMapping(method = PATCH)
    @Secured("ROLE_USER")
    public void register(@Validated @RequestBody final UpdateUserDTO user) throws UserAlreadyExistsException
    {
        userFacade.update(user);
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorDTO handleUserAlreadyExistsException(UserAlreadyExistsException exception)
    {
        return errorConverter.convert(exception);
    }
}
