package com.emirovschi.sm.lab5.posts;

public class PostConstants
{
    public static final String SEARCH_POSTS = "SELECT post "
            + "FROM PostModel post "
            + "WHERE (:firstId IS NULL OR post.id <= :firstId) "
            + "AND ((:adds) IS NULL OR "
            + ":addsCount = (SELECT COUNT(iTag) FROM PostModel iPost INNER JOIN iPost.tags iTag WHERE iPost = post AND iTag IN (:adds))) "
            + "AND ((:excludes) IS NULL OR "
            + "NOT EXISTS (SELECT iTag FROM PostModel iPost INNER JOIN iPost.tags iTag WHERE iPost = post AND iTag IN (:excludes))) "
            + "ORDER BY post.id DESC";
}
