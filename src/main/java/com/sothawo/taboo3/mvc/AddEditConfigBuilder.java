/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.mvc;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public final class AddEditConfigBuilder {
    private String caption;
    private String buttonLabel;
    private String mode;

    private AddEditConfigBuilder() {
    }

    public static AddEditConfigBuilder anAddEditConfig() {
        return new AddEditConfigBuilder();
    }

    public AddEditConfigBuilder withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public AddEditConfigBuilder withButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
        return this;
    }

    public AddEditConfigBuilder withMode(String mode) {
        this.mode = mode;
        return this;
    }

    public AddEditConfig build() {
        AddEditConfig addEditConfig = new AddEditConfig();
        addEditConfig.setCaption(caption);
        addEditConfig.setButtonLabel(buttonLabel);
        addEditConfig.setMode(mode);
        return addEditConfig;
    }
}
