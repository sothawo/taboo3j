/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@ControllerAdvice
public class AllControllers {

    @ModelAttribute("userName")
    public String addUserName(@AuthenticationPrincipal Principal principal) {
        return (null != principal) ? principal.getName() : null;
    }
}
