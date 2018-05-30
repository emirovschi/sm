package com.emirovschi.sm.lab5.tags;

import com.emirovschi.sm.lab5.tags.models.TagModel;

import java.util.List;
import java.util.Set;

public interface TagService
{
    List<TagModel> save(List<String> tags);

    Set<TagModel> getTags(Set<String> tags);

    List<TagModel> getTags(String query, Set<TagModel> adds, Set<TagModel> excludes);
}
