/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Configuration
@EnableElasticsearchRepositories("com.sothawo.taboo3.data")
public class ElasticConfig {
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }
}
