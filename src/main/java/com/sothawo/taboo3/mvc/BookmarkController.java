/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import com.sothawo.taboo3.data.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller to handle single bookmarks (add, edit, delete).
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Controller
@RequestMapping("/bookmark")
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    /**
     * display the given Bookmark and request deletion confirmation. If no bookmarks is found, the user is redirected
     * to the bookmark list size.
     *
     * @return ModelAndView for the confirm page.
     */
    @GetMapping("/delete/{id}")
    public ModelAndView showForDelete(@PathVariable String id) {
        logger.info("delete view requested for id {}", id);
        return bookmarkService.findById(id)
                .map(bookmark -> new ModelAndView("delete")
                        .addObject("bookmark", bookmark))
                .orElse(new ModelAndView("redirect:/"));
    }

    /**
     * called to do a delete.
     *
     * @param id
     *         the id of the bookmark to delete
     * @return redirect to the list view
     */
    @PostMapping("/delete/{id}")
    public ModelAndView doDelete(@PathVariable String id) {
        logger.info("deleting bookmark with id {}", id);
        try {
            bookmarkService.deleteBookmark(id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return new ModelAndView("redirect:/");
    }
}
