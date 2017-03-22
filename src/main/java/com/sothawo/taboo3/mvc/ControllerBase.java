/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * common functionality for controllers.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
public class ControllerBase {
    /**
     * adds a userName to the model if it is present.
     *
     * @param principal
     *         the principal where to take the username from
     * @param mav
     *         the ModelAndView
     */
    protected void addUserNameToModel(@AuthenticationPrincipal Principal principal, ModelAndView mav) {
        if (null != principal) {
            mav.addObject("userName", principal.getName());
        }
    }
}
