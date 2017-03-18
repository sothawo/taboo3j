/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public interface BookmarkRepository extends ElasticsearchCrudRepository<Bookmark, String> {
    @NotNull
    Collection<Bookmark> findByOwner(@NotNull String owner);

    @NotNull
    Collection<Bookmark> findByTitleContaining(@NotNull String text);

    @NotNull
    Collection<Bookmark> findByOwnerAndTitleContaining(@NotNull String owner, @NotNull String text);

    @NotNull
    Collection<Bookmark> findByTagsIn(@NotNull Collection<String> tags);

    @NotNull
    Collection<Bookmark> findByOwnerAndTagsIn(@NotNull String owner, @NotNull Collection<String> tags);

    @NotNull
    Collection<Bookmark> findByTitleContainingAndTagsIn(@NotNull String text, @NotNull Collection<String> tags);

    @NotNull
    Collection<Bookmark> findByOwnerAndTitleContainingAndTagsIn(@NotNull String owner, @NotNull String text,
                                                              @NotNull List<String> tags);
}
