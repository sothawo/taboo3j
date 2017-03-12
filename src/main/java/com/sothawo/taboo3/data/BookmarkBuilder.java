/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import java.util.Collection;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public final class BookmarkBuilder {
    private String owner;
    private String url;
    private String description;
    private Collection<String> tags;

    private BookmarkBuilder() {
    }

    public static BookmarkBuilder aBookmark() {
        return new BookmarkBuilder();
    }

    public BookmarkBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public BookmarkBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public BookmarkBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BookmarkBuilder withTags(Collection<String> tags) {
        this.tags = tags;
        return this;
    }

    public Bookmark build() {
        Bookmark bookmark = new Bookmark();
        bookmark.setOwner(owner);
        bookmark.setUrl(url);
        bookmark.setDescription(description);
        bookmark.setTags(tags);
        return bookmark;
    }
}
