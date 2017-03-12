/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public final class BookmarkBuilder {
    private String owner;
    private String url = "";
    private String title = "";
    private  Collection<String> tags = new HashSet<>();

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

    public BookmarkBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookmarkBuilder withTags(Collection<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public Bookmark build() {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(owner + '-' + url);
        bookmark.setOwner(owner);
        bookmark.setUrl(url);
        bookmark.setTitle(title);
        tags.forEach(bookmark::addTag);
        return bookmark;
    }
}
