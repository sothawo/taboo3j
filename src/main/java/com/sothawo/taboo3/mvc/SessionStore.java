/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;


import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * session scoped object to store the currently selections and bookmarks.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Component
@SessionScope
public class SessionStore {
    private static final Logger logger = LoggerFactory.getLogger(SessionStore.class);

    /** time when the store was created. */
    private final LocalDateTime creationTime = LocalDateTime.now();

    /** the selected tags. */
    private final Set<String> selectedTags = new HashSet<>();

    /** the search text. */
    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return "SessionStore{" +
                "creationTime=" + creationTime +
                ", selectedTags=" + selectedTags +
                ", searchText='" + searchText + '\'' +
                '}';
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * checks wether some criteria for selecting bookmarks are set.
     *
     * @return true if criteria are set
     */
    public boolean hasSelectCriteria() {
        return selectedTags.size() > 0 || (null != searchText && !searchText.isEmpty());
    }

    public Set<String> getSelectedTags() {
        return selectedTags;
    }

    /**
     * adds a tag to to the set of selected tags
     *
     * @param tag
     *         the tag to add, if null it is ignored
     */
    public void addSelectedTag(@Nullable String tag) {
        if (null != tag) {
            selectedTags.add(tag);
        }
    }

    /**
     * removes a tag from the set of selected tags
     *
     * @param tag
     */
    public void removeSelectedTag(String tag) {
        if (null != tag) {
            selectedTags.remove(tag);
        }
    }

    /**
     * clears the selection data.
     */
    public void clearSelection() {
        searchText = null;
        selectedTags.clear();
    }
}
