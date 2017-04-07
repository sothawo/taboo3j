/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.mvc;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public final class LoadTitleRequestBuilder {
    private String url;

    private LoadTitleRequestBuilder() {
    }

    public static LoadTitleRequestBuilder aLoadTitleRequest() {
        return new LoadTitleRequestBuilder();
    }

    public LoadTitleRequestBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public LoadTitleRequest build() {
        LoadTitleRequest loadTitleRequest = new LoadTitleRequest();
        loadTitleRequest.setUrl(url);
        return loadTitleRequest;
    }
}
