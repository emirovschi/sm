package com.emirovschi.sm.lab5.tags.converters;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.list.converters.ListConverter;
import com.emirovschi.sm.lab5.tags.dto.TagDTO;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagListConverter extends ListConverter<TagModel, TagDTO>
{
    @Autowired
    private Converter<TagModel, TagDTO> tagConverter;

    @Override
    protected Converter<TagModel, TagDTO> getItemConverter()
    {
        return tagConverter;
    }
}
