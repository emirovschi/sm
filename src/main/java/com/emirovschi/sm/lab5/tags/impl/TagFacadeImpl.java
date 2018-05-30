package com.emirovschi.sm.lab5.tags.impl;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.list.dto.ListDTO;
import com.emirovschi.sm.lab5.search.Search;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import com.emirovschi.sm.lab5.tags.TagFacade;
import com.emirovschi.sm.lab5.tags.TagService;
import com.emirovschi.sm.lab5.tags.dto.TagDTO;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagFacadeImpl implements TagFacade
{
    @Autowired
    private TagService tagService;

    @Autowired
    private Converter<SearchDTO, Search> searchReverseConverter;

    @Autowired
    private Converter<List<TagModel>, ListDTO<TagDTO>> tagListConverter;

    @Override
    public ListDTO<TagDTO> searchTags(final SearchDTO searchDTO)
    {
        final Search search = searchReverseConverter.convert(searchDTO);

        return tagListConverter.convert(tagService.getTags(search.getQuery(), search.getAdds(), search.getExcludes()));
    }
}
