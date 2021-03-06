/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;

/**
 * data for a search request.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class SearchData {
    private String text;

    public SearchData() {
    }

    public SearchData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "text='" + text + '\'' +
                '}';
    }
}
