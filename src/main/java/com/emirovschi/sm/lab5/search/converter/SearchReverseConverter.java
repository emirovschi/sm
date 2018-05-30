package com.emirovschi.sm.lab5.search.converter;

import com.emirovschi.sm.lab5.converters.Converter;
import com.emirovschi.sm.lab5.search.Search;
import com.emirovschi.sm.lab5.search.dto.SearchDTO;
import com.emirovschi.sm.lab5.tags.TagService;
import com.emirovschi.sm.lab5.tags.dto.TagDTO;
import com.emirovschi.sm.lab5.tags.models.TagModel;
import com.emirovschi.sm.lab5.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@Component
public class SearchReverseConverter implements Converter<SearchDTO, Search>
{
    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Override
    public Search convert(final SearchDTO searchDTO)
    {
        final Search search = new Search();
        search.setQuery(getString(searchDTO.getQuery()));
        search.setAdds(getSet(searchDTO.getTags(), getTags(true)));
        search.setExcludes(getSet(searchDTO.getTags(), getTags(false)));
        search.setUsers(getSet(searchDTO.getUsers(), userService::getUsers));
        search.setFirstId(searchDTO.getFirstId());
        return search;
    }

    private String getString(final String string)
    {
        return ofNullable(string).filter(s -> !s.isEmpty()).map(s -> "%" + s + "%").orElse(null);
    }

    private <D, T> Set<T> getSet(final Set<D> set, Function<Set<D>, Set<T>> function)
    {
        return ofNullable(set).map(function).filter(s -> !s.isEmpty()).orElse(null);
    }

    private Function<Set<TagDTO>, Set<TagModel>> getTags(final boolean add)
    {
        return tags -> tagService.getTags(tags.stream()
                .filter(t -> t.isAdd() == add)
                .map(TagDTO::getName)
                .collect(toSet()));
    }
}
