/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;

import com.sothawo.taboo3.data.BookmarkEdit;
import com.sothawo.taboo3.data.BookmarkService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static com.sothawo.taboo3.mvc.AddEditConfigBuilder.anAddEditConfig;


/**
 * Controller to handle single bookmarks (add, edit, delete).
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
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

    /**
     * displays a bookmark for editing.
     *
     * @param id
     *         the id of the bookmark.
     * @return MOdelAndView for editing or redirect to home page if bookmark is not found
     */
    @GetMapping("/edit/{id}")
    public ModelAndView showForEdit(@PathVariable String id) {
        logger.info("edit view requested for id {}", id);
        return bookmarkService.findById(id)
                .map(bookmark -> new ModelAndView("edit")
                        .addObject("bookmark", new BookmarkEdit(bookmark))
                        .addObject("config", anAddEditConfig().withCaption("edit bookmark").withButtonLabel("update")
                                .withMode("edit").build()))
                .orElse(new ModelAndView("redirect:/"));

    }

    /**
     * does the editing/adding
     *
     * @param principal
     *         the user, needed to insert owner in add
     * @param bookmarkEdit
     *         the edited/new  bookmark.
     * @param mode
     *         the mode, update or add
     * @return ModelAndView with redirect to the main page.
     */
    @PostMapping("/edit")
    public ModelAndView doUpdate(@AuthenticationPrincipal Principal principal, BookmarkEdit bookmarkEdit,
                                 @RequestParam("mode") String mode) {
        final String url = bookmarkEdit.getUrl();
        if (null == url || url.isEmpty()) {
            // todo: error message
            return new ModelAndView("redirect:/bookmark/edit/" + bookmarkEdit.getOriginalId());
        }
        if (!url.startsWith("http")) {
            bookmarkEdit.setUrl("http://" + url);
        }
        if ("edit".equals(mode)) {
            logger.info("updating {}", bookmarkEdit);
            if (!bookmarkEdit.getOriginalId().equals(bookmarkEdit.getId())) {
                bookmarkService.deleteBookmark(bookmarkEdit.getOriginalId());
            }
            bookmarkService.save(bookmarkEdit.getBookmark());
        } else if ("add".equals(mode)) {
            bookmarkEdit.setOwner(principal.getName());
            logger.info("inserting {}", bookmarkEdit);
            bookmarkService.save(bookmarkEdit.getBookmark());
        }
        return new ModelAndView("redirect:/");
    }

    /**
     * shows the view for adding a bookmark.
     *
     * @return ModelAndView for editiing with a new empty BookmarkEdit object.
     */
    @GetMapping("/add")
    public ModelAndView showForAdd() {
        return new ModelAndView("edit").addObject("bookmark", new BookmarkEdit())
                .addObject("config",
                        anAddEditConfig().withCaption("add bookmark").withButtonLabel("add").withMode("add").build());
    }

    /**
     * extracts the title for a webpage.
     *
     * @param loadTitleRequest
     *         the request object conatining the url.
     * @return the title or empty string if it cannot be loaded
     */
    @PostMapping("/loadtitle")
    @ResponseBody
    @NotNull
    public String loadTitle(LoadTitleRequest loadTitleRequest) {
        logger.info("load title requested for {}", loadTitleRequest);
        return "hello";
    }
}
