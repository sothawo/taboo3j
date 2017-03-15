/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * implementation of the BookmarkRepositorsy interface. uses a standard spring-data repository and a custom
 * ElasticsearchOperations object that operate on the same elasticsearch instance.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Component
public class BookmarkRepositoryImpl implements BookmarkRepository {

    /** the standard spring-data repository. */
    private final BookmarkElasticRepository bookmarkElasticRepository;

    /** for custom queries. */
    private final ElasticsearchOperations elasticsearchTemplate;

    @Autowired
    public BookmarkRepositoryImpl(BookmarkElasticRepository bookmarkElasticRepository,
                                  ElasticsearchOperations elasticsearchTemplate) {
        this.bookmarkElasticRepository = bookmarkElasticRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public void deleteAll() {
        bookmarkElasticRepository.deleteAll();
    }

    @Override
    public void save(@NotNull Bookmark bookmark) {
        bookmarkElasticRepository.save(bookmark);
    }

    @Override
    public void save(@NotNull Iterable<Bookmark> bookmarks) {
        bookmarkElasticRepository.save(bookmarks);
    }

    @NotNull
    @Override
    public Collection<Bookmark> findAll() {
        return StreamSupport.stream(bookmarkElasticRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public Collection<String> findAllTags() {
        return StreamSupport.stream(bookmarkElasticRepository.findAll().spliterator(), false).map(Bookmark::getTags)
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public List<Bookmark> findByOwner(@NotNull String owner) {
        return bookmarkElasticRepository.findByOwner(owner);
    }
}
