package com.emirovschi.sm.lab5.posts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PostConfiguration
{
    @Bean("allowedContentTypes")
    public Set<String> getAllowedContentTypes()
    {
        final Set<String> allowedContentTypes = new HashSet<>();
        allowedContentTypes.add(MediaType.IMAGE_JPEG_VALUE);
        allowedContentTypes.add(MediaType.IMAGE_PNG_VALUE);
        allowedContentTypes.add("video/mp4");
        allowedContentTypes.add("audio/mp3");
        return allowedContentTypes;
    }
}
