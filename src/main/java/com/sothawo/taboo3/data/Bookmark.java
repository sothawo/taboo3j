/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Collection;
import java.util.Objects;

/**
 * ElasticSearch bookmark document.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Document(indexName = "bookmarks")
public class Bookmark {
    @Id
    private String url;
    private String owner;
    private String description;
    private Collection<String> tags;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(url, bookmark.url) &&
                Objects.equals(owner, bookmark.owner) &&
                Objects.equals(description, bookmark.description) &&
                Objects.equals(tags, bookmark.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, owner, description, tags);
    }
}
