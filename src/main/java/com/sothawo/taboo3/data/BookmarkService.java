/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
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
    private final BookmarkRepository bookmarkElasticRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkElasticRepository) {
        this.bookmarkElasticRepository = bookmarkElasticRepository;
    }

    /**
     * delete all entries from the repository.
     */
    public void deleteAll() {
        bookmarkElasticRepository.deleteAll();
    }

    /**
     * delete all entries for an owner.
     *
     * @param owner
     *         the woner
     */
    public void deleteByOwner(@NotNull String owner) {
        bookmarkElasticRepository.delete(bookmarkElasticRepository.findByOwner(owner));
    }

    /**
     * save a bookmark.
     *
     * @param bookmark
     *         the bookmark to save
     */
    public void save(@NotNull Bookmark bookmark) {
        bookmarkElasticRepository.save(bookmark);
    }

    /**
     * save bookmarks.
     *
     * @param bookmarks
     *         the bookmarks to save
     */
    public void save(@NotNull Iterable<Bookmark> bookmarks) {
        bookmarkElasticRepository.save(bookmarks);
    }

    /**
     * returns all bookmarks.
     *
     * @return collection of bookmarks
     */
    @NotNull
    public Collection<Bookmark> findAll() {
        return StreamSupport.stream(bookmarkElasticRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * gets all the tags from the repository.
     *
     * @return collection of tags
     */
    @NotNull
    public Collection<String> findAllTags() {
        return StreamSupport.stream(bookmarkElasticRepository.findAll().spliterator(), false).map(Bookmark::getTags)
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
        return bookmarkElasticRepository.findByOwner(owner);
    }

    /**
     * deletes a bookmark.
     *
     * @param bookmark
     *         the bookmark to delete
     */
    public void deleteBookmark(@NotNull Bookmark bookmark) {
        bookmarkElasticRepository.delete(bookmark);
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
        return StreamSupport.stream(bookmarkElasticRepository.findByOwner(owner).spliterator(), false)
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
        return Optional.ofNullable(bookmarkElasticRepository.findOne(id));
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
        return bookmarkElasticRepository.findByTitleContaining(text);
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
        return bookmarkElasticRepository.findByOwnerAndTitleContaining(owner, text);
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
        return bookmarkElasticRepository.findByTagsIn(tags).stream()
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
        return bookmarkElasticRepository.findByOwnerAndTagsIn(owner, tags).stream().
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
        return bookmarkElasticRepository.findByTitleContainingAndTagsIn(text, tags).stream()
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
                                                           @NotNull List<String> tags) {
        return bookmarkElasticRepository.findByOwnerAndTitleContainingAndTagsIn(owner, text, tags).stream()
                .filter(bookmark -> bookmark.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }
}
