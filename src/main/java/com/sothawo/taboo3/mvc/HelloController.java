/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.mvc;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * dummy controller to get the program started.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Controller
@RequestMapping({"/", "/hello"})
public class HelloController {
    @GetMapping
    public ModelAndView hello(@AuthenticationPrincipal String principal) {
        return new ModelAndView("hello", "user", principal);
    }
}
