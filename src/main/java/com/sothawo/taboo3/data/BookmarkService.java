/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service to manage Bookmarks using an ElasticSearch repository.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Component
public class BookmarkService {

    /** the standard spring-data repository. */
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    /**
     * delete all entries from the repository.
     */
    public void deleteAll() {
        bookmarkRepository.deleteAll();
    }

    /**
     * delete all entries for an owner.
     *
     * @param owner
     *         the woner
     */
    public void deleteByOwner(@NotNull String owner) {
        bookmarkRepository.delete(bookmarkRepository.findByOwner(owner));
    }

    /**
     * save a bookmark.
     *
     * @param bookmark
     *         the bookmark to save
     */
    public void save(@NotNull Bookmark bookmark) {
        bookmarkRepository.save(bookmark);
    }

    /**
     * save bookmarks.
     *
     * @param bookmarks
     *         the bookmarks to save
     */
    public void save(@NotNull Iterable<Bookmark> bookmarks) {
        bookmarkRepository.save(bookmarks);
    }

    /**
     * returns all bookmarks.
     *
     * @return collection of bookmarks
     */
    @NotNull
    public Collection<Bookmark> findAll() {
        return StreamSupport.stream(bookmarkRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * gets all the tags from the repository.
     *
     * @return collection of tags
     */
    @NotNull
    public Collection<String> findAllTags() {
        return StreamSupport.stream(bookmarkRepository.findAll().spliterator(), false).map(Bookmark::getTags)
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * gets all bookmarks for a given owner.
     *
     * @param owner
     *         the owner
     * @return the bookmarks for the owner
     */
    @NotNull
    public Collection<Bookmark> findByOwner(@NotNull String owner) {
        return bookmarkRepository.findByOwner(owner);
    }

    /**
     * deletes a bookmark.
     *
     * @param bookmark
     *         the bookmark to delete
     */
    public void deleteBookmark(@NotNull Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    /**
     * returns all distinct tags from the bookmarks belnging to an owner.
     *
     * @param owner
     *         the owner
     * @return the tags
     */
    @NotNull
    public Collection<String> findAllTagsByOwner(@NotNull String owner) {
        return StreamSupport.stream(bookmarkRepository.findByOwner(owner).spliterator(), false)
                .map(Bookmark::getTags).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * retrievs a Bookmark by its id
     *
     * @param id
     *         the id
     * @return the bookmark
     */
    @NotNull
    public Optional<Bookmark> getBookmarkById(@NotNull String id) {
        return Optional.ofNullable(bookmarkRepository.findOne(id));
    }

    /**
     * returns all bookmarks that contain a given text in their title.
     *
     * @param text
     *         the text to search
     * @return the found bookmarks
     */
    @NotNull
    public Collection<Bookmark> findByTitle(@NotNull String text) {
        return bookmarkRepository.findByTitleContaining(text);
    }

    /**
     * returns all bookmarks for a owner that contain a given text in their title.
     *
     * @param owner
     *         the ownwe
     * @param text
     *         the text to search
     * @return the found bookmarks
     */
    @NotNull
    public Collection<Bookmark> findByOwnerAndTitle(@NotNull String owner, @NotNull String text) {
        return bookmarkRepository.findByOwnerAndTitleContaining(owner, text);
    }

    /**
     * finds all bookmarks which match the given tags
     *
     * @param tags
     *         the tags to match
     * @return Collection of boookmarks
     */
    @NotNull
    public Collection<Bookmark> findByTags(@NotNull Collection<String> tags) {
        return bookmarkRepository.findByTagsIn(tags).stream()
                .filter(bookmark -> bookmark.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    /**
     * finds all bookmarks which match the given tags
     *
     * @param tags
     *         the tags to match
     * @return Collection of boookmarks
     */
    @NotNull
    public Collection<Bookmark> findByOwnerAndTags(@NotNull String owner, @NotNull Collection<String> tags) {
        return bookmarkRepository.findByOwnerAndTagsIn(owner, tags).stream().
                filter(bookmark -> bookmark.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    /**
     * finds all bookmarks which have a text in the title and match the given tags.
     *
     * @param text
     *         the text to search
     * @param tags
     *         the tags to match
     * @return Collection of boookmarks
     */
    @NotNull
    public Collection<Bookmark> findByTitleAndTags(@NotNull String text, @NotNull Collection<String> tags) {
        return bookmarkRepository.findByTitleContainingAndTagsIn(text, tags).stream()
                .filter(bookmark -> bookmark.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    /**
     * finds all bookmarks for an owner which have a text in the title and match the given tags.
     *
     * @param owner
     *         the owner
     * @param text
     *         the text to search
     * @param tags
     *         the tags to match
     * @return Collection of boookmarks
     */
    @NotNull
    public Collection<Bookmark> findByOwnerAndTitleAndTags(@NotNull String owner, @NotNull String text,
                                                           @NotNull Collection<String> tags) {
        return bookmarkRepository.findByOwnerAndTitleContainingAndTagsIn(owner, text, tags).stream()
                .filter(bookmark -> bookmark.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    /**
     * finds a bookmark by its id.
     *
     * @param id
     *         the id
     * @return the optional bookmark
     */
    public Optional<Bookmark> findById(String id) {
        return Optional.ofNullable(bookmarkRepository.findOne(id));
    }

    /**
     * deletes a bookmark idfentified by its id
     *
     * @param id
     *         the id
     */
    public void deleteBookmark(String id) {
        bookmarkRepository.delete(id);
    }
}
