/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;

import com.sothawo.taboo3.data.Bookmark;
import com.sothawo.taboo3.data.BookmarkEdit;
import com.sothawo.taboo3.data.BookmarkService;
import org.jetbrains.annotations.NotNull;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;

import static com.sothawo.taboo3.mvc.AddEditConfigBuilder.anAddEditConfig;
import static com.sothawo.taboo3.mvc.LoadTitleRequestBuilder.aLoadTitleRequest;


/**
 * Controller to handle single bookmarks (add, edit, delete).
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Controller
@RequestMapping("/bookmark")
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    /**
     * user agent that jsoup sends when fetching the page title. Some sites send 403, when no known user agent is
     * sent).
     */
    private static final String JSOUP_USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

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
     * @param url
     *         optional argument to preload a url, used for bookmarklet
     * @return ModelAndView for editiing with a new empty BookmarkEdit object.
     */
    @GetMapping("/add")
    public ModelAndView showForAdd(@RequestParam(required = false) String url) {
        final BookmarkEdit bookmarkEdit = new BookmarkEdit();
        if (null != url) {
            bookmarkEdit.setUrl(url);
            final ResponseEntity<String> responseEntity = loadTitle(aLoadTitleRequest().withUrl(url).build());
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                bookmarkEdit.setTitle(responseEntity.getBody());
            }
        }
        return new ModelAndView("edit").addObject("bookmark", bookmarkEdit)
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
    public ResponseEntity<String> loadTitle(LoadTitleRequest loadTitleRequest) {
        String urlString = loadTitleRequest.getUrl();
        if (null != urlString && !urlString.isEmpty()) {
            if (!urlString.startsWith("http")) {
                urlString = "http://" + urlString;
            }
            final String finalUrl = urlString;
            logger.info("loading title for url {}", finalUrl);
            try {
                String htmlTitle = Jsoup
                        .connect(finalUrl)
                        .timeout(5000)
                        .userAgent(JSOUP_USER_AGENT)
                        .get()
                        .title();
                logger.info("got title: {}", htmlTitle);
                return new ResponseEntity<>(htmlTitle, HttpStatus.OK);
            } catch (HttpStatusException e) {
                logger.info("loading url http error", e);
                return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode()));
            } catch (IOException e) {
                logger.info("loading url error", e);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * inserts a whole array of bookmarks into the service. id values contained in the repository are recalculated by
     * setting the owner to the principal.
     *
     * @param principal
     *         the user calling the service
     * @param bookmarks
     *         the bookmarks to insert
     * @return status code with message
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> upload(@AuthenticationPrincipal Principal principal,
                                         @RequestBody Bookmark[] bookmarks) {
        logger.info("should upload {} bookmarks", bookmarks.length);
        for (Bookmark bookmark : bookmarks) {
            bookmark.setOwner(principal.getName());
        }
        bookmarkService.save(Arrays.asList(bookmarks));
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * dumps all bookmarks for a given principal.
     *
     * @param principal
     *         the principal whose bookmarks are to be dumped.
     * @return possibliy empty list of bookmarks
     */
    @GetMapping("/dump")
    @ResponseBody
    public ResponseEntity<Collection<Bookmark>> dumpBookmarks(@AuthenticationPrincipal Principal principal) {
        final Collection<Bookmark> bookmarks = bookmarkService.findByOwner(principal.getName());
        return new ResponseEntity<>(bookmarks, HttpStatus.OK);
    }
}
