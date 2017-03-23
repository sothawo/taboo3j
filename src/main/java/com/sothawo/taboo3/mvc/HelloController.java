/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * dummy controller to get the program started.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Controller
@RequestMapping({"/hello"})
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    private final SessionStore sessionStore;

    @Autowired
    public HelloController(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    /**
     * Username is added by the AllControllers advice.
     *
     * @return model and view
     */
    @GetMapping
    public ModelAndView hello() {
        logger.info("SessionStore: {}", sessionStore);
        return new ModelAndView("hello");
    }

}
