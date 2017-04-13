/*
 * (c) Copyright 2017 sothawo
 */
package com.sothawo.taboo3.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static com.sothawo.taboo3.data.BookmarkBuilder.aBookmark;

/**
 * Component to setup a couple of Bookmarks for user peter if the repository is empty.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Component
public class TestInitializerWork {

    private static final Logger logger = LoggerFactory.getLogger(TestInitializerWork.class);

    private final BookmarkService bookmarkService;

    @Autowired
    public TestInitializerWork(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

//    @PostConstruct
    void createBookmarksIfNecessary() {
        final String owner = "work";
        final int numBookmarks = bookmarkService.findByOwner(owner).size();
        logger.info("repository has {} entries for user {}", numBookmarks, owner);
        if (0 == numBookmarks) {
            logger.info("setting up repository");
            final Bookmark bookmark1 =
                    aBookmark()
                            .withOwner(owner)
                            .withUrl("http://www.postdirekt.de")
                            .withTitle("Deutsche Post Direkt")
                            .addTag("work")
                            .build();
            final Bookmark bookmark2 =
                    aBookmark()
                            .withOwner(owner)
                            .withUrl("http://www.jaroso.de")
                            .withTitle("Jaroso")
                            .addTag("work")
                            .build();
            final Bookmark bookmark3 =
                    aBookmark()
                            .withOwner(owner)
                            .withUrl("http://www.hlx.com")
                            .withTitle("HLX")
                            .addTag("work")
                            .build();
            bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));

            logger.info("repository now has {} entries for user {}", bookmarkService.findByOwner(owner).size(), owner);
        }
        bookmarkService.findByOwner(owner).forEach(bookmark -> logger.debug(bookmark.toString()));
    }
}
