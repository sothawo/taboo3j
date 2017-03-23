/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import com.sothawo.taboo3.data.Bookmark;
import com.sothawo.taboo3.data.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Controller
@RequestMapping({"/", "/bookmarks"})
public class BookmarksController {
    private static final Logger logger = LoggerFactory.getLogger(BookmarksController.class);

    private final SessionStore sessionStore;
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarksController(SessionStore sessionStore, BookmarkService bookmarkService) {
        this.sessionStore = sessionStore;
        this.bookmarkService = bookmarkService;
    }

    /**
     * returns all bookmarks to display.
     *
     * @return model data and view name
     */
    @GetMapping
    public ModelAndView allBookmarks(@AuthenticationPrincipal Principal principal) {
        String owner = (null != principal) ? principal.getName() : null;
        ModelAndView mav = new ModelAndView("bookmarks");
        if (null != owner) {
            final Collection<Bookmark> bookmarks = bookmarkService.findByOwner(owner);
            logger.info("got {} bookmarks for owner {}", bookmarks.size(), owner);
            mav.addObject("bookmarks", bookmarks);

            List<String> tags = bookmarks.stream().map(Bookmark::getTags)
                    .flatMap(Collection::stream).collect(Collectors.toSet())
                    .stream().sorted().collect(Collectors.toList());
            mav.addObject("availableTags", tags);
        }
        return mav;
    }
}
