package com.emirovschi.sm.lab5.search;

import com.emirovschi.sm.lab5.tags.models.TagModel;
import com.emirovschi.sm.lab5.users.models.UserModel;

import java.util.Set;

public class Search
{
    private String query;
    private Set<TagModel> adds;
    private Set<TagModel> excludes;
    private Set<UserModel> users;
    private Long firstId;

    public String getQuery()
    {
        return query;
    }

    public void setQuery(final String query)
    {
        this.query = query;
    }

    public Set<TagModel> getAdds()
    {
        return adds;
    }

    public void setAdds(final Set<TagModel> adds)
    {
        this.adds = adds;
    }

    public Set<TagModel> getExcludes()
    {
        return excludes;
    }

    public void setExcludes(final Set<TagModel> excludes)
    {
        this.excludes = excludes;
    }

    public Set<UserModel> getUsers()
    {
        return users;
    }

    public void setUsers(final Set<UserModel> users)
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
