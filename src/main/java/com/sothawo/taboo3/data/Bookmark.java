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

/**
 * The bookmark POJO. Tags when added are converted to lowercase and duplicate tags are removed. The Id is built by
 * concatenating the owner and the url.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Document(indexName = "bookmarks")
public final class Bookmark {

    /** the id. */
    @Id
    private String id;
    /** the owner of the bookmark. */
    private String owner;
    /** the URL the bookmark points to as String. */
    private String url = "";
    /** the title of a bookmark. */
    private String title = "";
    /** the tags of the bookmark. */
    private final Collection<String> tags = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        tags.add(Objects.requireNonNull(tag).toLowerCase());
    }

    /**
     * returns an unmodifiable view of the tags collection.
     *
     * @return unmodifiable collection
     */
    public Collection<String> getTags() {
        return Collections.unmodifiableCollection(tags);
    }
}
