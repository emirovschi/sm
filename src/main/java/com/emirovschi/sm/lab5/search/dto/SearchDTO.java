package com.emirovschi.sm.lab5.search.dto;

import com.emirovschi.sm.lab5.tags.dto.TagDTO;

import java.util.Set;

public class SearchDTO
{
    private String query;
    private Set<TagDTO> tags;
    private Set<String> users;
    private Long firstId;

    public String getQuery()
    {
        return query;
    }

    public void setQuery(final String query)
    {
        this.query = query;
    }

    public Set<TagDTO> getTags()
    {
        return tags;
    }

    public void setTags(final Set<TagDTO> tags)
    {
        this.tags = tags;
    }

    public Set<String> getUsers()
    {
        return users;
    }

    public void setUsers(final Set<String> users)
    {
        this.users = users;
    }

    public Long getFirstId()
    {
        return firstId;
    }

    public void setFirstId(final Long firstId)
    {
        this.firstId = firstId;
    }
}
