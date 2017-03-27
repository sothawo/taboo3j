/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

/**
 * a class to configure the add/edit form.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */

public class AddEditConfig {
    /** the form caption. */
    private String caption;

    /** the button label. */
    private String buttonLabel;

    /** the mode. */
    private String mode;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
