/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

/**
 * data for a search request.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
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
