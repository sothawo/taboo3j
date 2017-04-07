/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;

/**
 * request data for a load title request.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class LoadTitleRequest {
    /** the url for which to load the title. */
    private String url;

    @Override
    public String toString() {
        return "LoadTitleRequest{" +
                "url='" + url + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
