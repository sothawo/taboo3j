/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.data;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A Bookmark class that has the tags as joined string, needed for editing.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
public class BookmarkEdit {
    /** the delegated bookmark. */
    private Bookmark bookmark = new Bookmark();
    /** the id of the original bookmark. */
    private String originalId = "";
    public BookmarkEdit() {
    }

    public BookmarkEdit(Bookmark bookmark) {
        this.bookmark = requireNonNull(bookmark);
        setOriginalId(bookmark.getId());
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public String getId() {
        return bookmark.getId();
    }

    public void setId(String id) {
        bookmark.setId(id);
    }

    public String getOwner() {
        return bookmark.getOwner();
    }

    public void setOwner(String owner) {
        bookmark.setOwner(owner);
    }

    public String getUrl() {
        return bookmark.getUrl();
    }

    public void setUrl(String url) {
        bookmark.setUrl(url);
    }

    public String getTitle() {
        return bookmark.getTitle();
    }

    public void setTitle(String title) {
        bookmark.setTitle(title);
    }

    @Override
    public String toString() {
        return "BookmarkEdit{" +
                "bookmark=" + bookmark +
                ", originalId='" + originalId + '\'' +
                '}';
    }

    public void addTag(String tag) {
        bookmark.addTag(tag);
    }

    public Collection<String> getTags() {
        return bookmark.getTags();
    }

    public void clearTags() {
        bookmark.clearTags();
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getTagsAsString() {
        return bookmark.joinedTags();
    }

    public void setTagsAsString(@Nullable String tagsAsString) {
        if (null != tagsAsString) {
            for (String tag : tagsAsString.split("[,;]\\s+")) {
                addTag(tag);
            }
        }
    }
}
