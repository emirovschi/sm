package com.emirovschi.sm.lab5.tags.impl;

import com.emirovschi.sm.lab5.tags.TagRepository;
import com.emirovschi.sm.lab5.tags.TagService;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class TagServiceImpl implements TagService
{
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<TagModel> save(final List<String> tags)
    {
        return tags.stream().map(this::getOrSave).collect(Collectors.toList());
    }

    private TagModel getOrSave(final String tagName)
    {
        return ofNullable(tagRepository.findByName(tagName)).orElseGet(() -> createAndSave(tagName));
    }

    private TagModel createAndSave(final String tagName)
    {
        final TagModel tag = new TagModel();
        tag.setName(tagName);
        tagRepository.save(tag);
        return tag;
    }

    @Override
    public Set<TagModel> getTags(final Set<String> tags)
    {
        return tags.stream().map(tagRepository::findByName).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public List<TagModel> getTags(final String query, final Set<TagModel> adds, final Set<TagModel> excludes)
    {
        return tagRepository.findTags(query, ofNullable(adds).map(Collection::size).orElse(0), adds, excludes);
    }
}
