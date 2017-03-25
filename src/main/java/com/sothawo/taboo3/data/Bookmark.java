/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo3.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The bookmark POJO. Tags when added are converted to lowercase and duplicate tags are removed. The Id is built by
 * concatenating the owner and the url.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Document(indexName = "bookmarks")
public final class Bookmark {

    /** the tags of the bookmark. */
    private final Collection<String> tags = new HashSet<>();
    /** the id. */
    @Id
    private String id;
    /** the owner of the bookmark. */
    private String owner;
    /** the URL the bookmark points to as String. */
    private String url = "";
    /** the title of a bookmark. */
    private String title = "";

    Bookmark() {
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (null == owner) {
            throw new IllegalArgumentException("owner must not be null");
        }
        this.owner = owner.toLowerCase();
        buildId();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (null == url) {
            throw new IllegalArgumentException("url must not be null");
        }
        this.url = url;
        buildId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    void buildId() {
        this.id = ((null == owner) ? "(null)" : owner.toLowerCase())
                + '-'
                + ((null == url) ? "(null)" : url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(id, bookmark.id);
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                '}';
    }

    /**
     * adds the given tag in lowercase to the internal collection, if it is not already present.
     *
     * @param tag
     *         new tag
     * @throws NullPointerException
     *         when tag is null
     */
    public void addTag(final String tag) {
        tags.add(requireNonNull(tag).toLowerCase());
    }

    /**
     * returns an unmodifiable view of the tags collection.
     *
     * @return unmodifiable collection
     */
    public Collection<String> getTags() {
        return Collections.unmodifiableCollection(tags);
    }

    /**
     * clears all tags.
     */
    public void clearTags() {
        tags.clear();
    }

    /**
     * join all strings together.
     *
     * @return all Strings joined
     */
    public String joinedTags() {
        return getTags().stream().collect(Collectors.joining(", "));
    }
}
