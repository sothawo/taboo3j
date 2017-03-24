/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * returns the model and view for the login page.
     *
     * @return view and model with messages.
     */
    @GetMapping
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        final ModelAndView mav = new ModelAndView("login");
        if (null != error) {
            mav.addObject("errorText", "invalid login data");
        }
        if (null != logout) {
            mav.addObject("msg", "successfully logged out.");
        }
        return mav;
    }
}
