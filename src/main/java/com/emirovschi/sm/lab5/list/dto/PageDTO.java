package com.emirovschi.sm.lab5.list.dto;

import java.util.List;

public class PageDTO<T>
{
    private int currentPage;
    private int pageSize;
    private int totalPage;
    private long totalItems;
    private List<T> items;

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(final int currentPage)
    {
        this.currentPage = currentPage;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(final int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getTotalPage()
    {
        return totalPage;
    }

    public void setTotalPage(final int totalPage)
    {
        this.totalPage = totalPage;
    }

    public long getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems(final long totalItems)
    {
        this.totalItems = totalItems;
    }

    public List<T> getItems()
    {
        return items;
    }

    public void setItems(final List<T> items)
    {
        this.items = items;
    }
}
