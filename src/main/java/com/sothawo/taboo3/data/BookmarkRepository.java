/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * interface defining the bookmark repository operations.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public interface BookmarkRepository {
    /**
     * delete all entries from the repository.
     */
    void deleteAll();

    /**
     * save a bookmark.
     *
     * @param bookmark
     *         the bookmark to save
     */
    void save(@NotNull Bookmark bookmark);

    /**
     * save bookmarks.
     *
     * @param bookmarks
     *         the bookmarks to save
     */
    void save(@NotNull Iterable<Bookmark> bookmarks);

    /**
     * returns all bookmarks.
     *
     * @return collection of bookmarks
     */
    @NotNull
    Collection<Bookmark> findAll();

    /**
     * gets all the tags from the repository.
     *
     * @return collection of tags
     */
    @NotNull
    Collection<String> findAllTags();

    /**
     * gets al bookmarks for a given owner.
     *
     * @param owner
     *         the owner
     * @return the bookmarks for the owner
     */
    @NotNull
    Collection<Bookmark> findByOwner(@NotNull String owner);

    /**
     * deletes a bookmark.
     *
     * @param bookmark
     *         the bookmark to delete
     */
    void deleteBookmark(@NotNull Bookmark bookmark);

    /**
     * find all tags for a specific owner.
     *
     * @param owner
     *         the owner
     * @return the tags
     */
    @NotNull
    Collection<String> findAllTagsByOwner(@NotNull String owner);
}
