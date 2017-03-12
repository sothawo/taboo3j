/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;


import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public interface BookmarkRepository extends ElasticsearchCrudRepository<Bookmark, String> {
    List<Bookmark> findByOwner(String owner);
}
