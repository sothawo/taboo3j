/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.Collection;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public interface BookmarkElasticRepository extends ElasticsearchCrudRepository<Bookmark, String> {
    /**
     * find all bookmarks for an owner.
     *
     * @param owner
     *         the owner
     * @return tthe bookmarks
     */
    @NotNull
    Collection<Bookmark> findByOwner(@NotNull String owner);

    /**
     * delete all bookmarks belonging to an owner
     *
     * @param owner
     *         the owner
     */
    void deleteByOwner(@NotNull String owner);
}
