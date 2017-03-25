/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import com.sothawo.taboo3.data.Bookmark;
import com.sothawo.taboo3.data.BookmarkService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for displaying the bookmarks list.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Controller
@RequestMapping("/")
public class ListController {
    private static final Logger logger = LoggerFactory.getLogger(ListController.class);

    private final SessionStore sessionStore;
    private final BookmarkService bookmarkService;

    @Autowired
    public ListController(SessionStore sessionStore, BookmarkService bookmarkService) {
        this.sessionStore = sessionStore;
        this.bookmarkService = bookmarkService;
    }

    /**
     * returns all bookmarks to display. Takes the slection criteria from the injected SessionStorage.
     *
     * @return model data and view name
     */
    @GetMapping
    public ModelAndView bookmarksList(@AuthenticationPrincipal Principal principal,
                                      @RequestParam(value = "selectTag", required = false) String selectTag,
                                      @RequestParam(value = "deselectTag", required = false) String deselectTag) {
        ModelAndView mav = new ModelAndView("list");

        String owner = (null != principal) ? principal.getName() : null;
        if (null != owner) {

            sessionStore.addSelectedTag(selectTag);
            sessionStore.removeSelectedTag(deselectTag);

            Collection<Bookmark> bookmarks = new ArrayList<>();
            final Collection<String> availableTags = new ArrayList<>();
            final Collection<String> selectedTags = sessionStore.getSelectedTags();

            final String searchText = sessionStore.getSearchText();
            if (sessionStore.hasSelectCriteria()) {
                if (null != searchText && !searchText.isEmpty()) {
                    if (selectedTags.isEmpty()) {
                        bookmarks = bookmarkService.findByOwnerAndTitle(owner, searchText);
                    } else {
                        bookmarks = bookmarkService.findByOwnerAndTitleAndTags(owner, searchText, selectedTags);
                    }
                } else {
                    bookmarks = bookmarkService.findByOwnerAndTags(owner, selectedTags);
                }
                // available tags are the tags from the bookmarks which are not in the available tags.
                bookmarks.stream()
                        .map(Bookmark::getTags).flatMap(Collection::stream)
                        .collect(Collectors.toSet())
                        .stream()
                        .filter(tag -> !selectedTags.contains(tag))
                        .forEach(availableTags::add);
            } else {
                // leave bookmarks empty

                mav.addObject("bookmarksMessage", "no selection.");

                // get all available tags
                availableTags.addAll(bookmarkService.findAllTagsByOwner(owner)
                        .stream().sorted().collect(Collectors.toList()));
            }

            // set all objects, eventually to empty lists
            mav.addObject("bookmarks", bookmarks);
            mav.addObject("availableTags", sortStrings(availableTags));
            mav.addObject("selectedTags", sortStrings(selectedTags));

            // need this to bind the form to
            mav.addObject("searchData", new SearchData(searchText));
        }
        return mav;
    }

    /**
     * sets the search text and calls the bookmarks method.
     *
     * @param searchData
     *         search parameters
     * @return redirectting ModelAndView
     */
    @PostMapping("/searchText")
    public ModelAndView searchText(SearchData searchData) {
        if (null != searchData && null != searchData.getText()) {
            final String searchText = searchData.getText();
            logger.info("setting search text to {}", searchText);
            sessionStore.setSearchText(searchText);
        }
        return new ModelAndView("redirect:/");
    }

    /**
     * clears the selection data.
     *
     * @return redirecting ModelAndView
     */
    @PostMapping("/clearSelection")
    public ModelAndView clearSelection() {
        sessionStore.clearSelection();
        return new ModelAndView("redirect:/");
    }

    /**
     * converts a String colleciton to a sorted list.
     *
     * @param c
     *         the collection
     * @return the sorted list
     */
    @NotNull
    private List<String> sortStrings(@NotNull Collection<String> c) {
        return c.stream().sorted().collect(Collectors.toList());
    }
}
