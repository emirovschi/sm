package com.emirovschi.sm.lab5.tags;

import com.emirovschi.sm.lab5.list.dto.ListDTO;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import com.emirovschi.sm.lab5.tags.dto.TagDTO;

public interface TagFacade
{
    ListDTO<TagDTO> searchTags(SearchDTO search);
}
