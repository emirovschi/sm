package com.emirovschi.sm.lab5.list.dto;

import java.util.List;

public class ListDTO<T>
{
    private List<T> items;

    public List<T> getItems()
    {
        return items;
    }

    public void setItems(final List<T> items)
    {
        this.items = items;
    }
}
