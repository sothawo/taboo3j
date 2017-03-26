package com.sothawo.taboo3.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.sothawo.taboo3.data.BookmarkBuilder.aBookmark;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class BookmarkServiceTest {

    @Autowired
    BookmarkService bookmarkService;

    @Before
    public void setup() {
        bookmarkService.deleteAll();
    }


    @Test
    public void insertAndRetrieve() throws Exception {

        Bookmark bookmark = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        bookmarkService.save(bookmark);

        Collection<Bookmark> bookmarks = bookmarkService.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark);
    }

    @Test
    public void sameUrlDifferentOwner() throws Exception {
        Bookmark bookmark1 = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTitle("this is the first entry")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        Bookmark bookmark2 = aBookmark()
                .withOwner("other")
                .withUrl("https://www.sothawo.com")
                .withTitle("this is the first entry")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));

        assertThat(bookmarkService.findByOwner("peter")).containsOnly(bookmark1);
        assertThat(bookmarkService.findByOwner("other")).containsOnly(bookmark2);
    }


    @Test
    public void initiallyEmpty() throws Exception {
        assertThat(bookmarkService.findAll()).isEmpty();
        assertThat(bookmarkService.findAllTags()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithoutUrl() throws Exception {
        bookmarkService.save(aBookmark().withOwner("owner").withTitle("title").build());
        fail("excpected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithoutOwner() throws Exception {
        bookmarkService.save(aBookmark().withUrl("url").withTitle("title").build());
        fail("excpected IllegalArgumentException");
    }

    @Test
    public void deleteExistingBookmark() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner").withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));
        bookmarkService.deleteBookmark(bookmark2);

        final Collection<Bookmark> bookmarks = bookmarkService.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test
    public void deleteExistingBookmarkById() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner").withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));
        bookmarkService.deleteBookmark(bookmark2.getId());

        final Collection<Bookmark> bookmarks = bookmarkService.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test
    public void deleteAllBookmarkForOwner() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));
        bookmarkService.deleteByOwner("owner1");

        final Collection<Bookmark> bookmarks = bookmarkService.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark2);
    }

    @Test
    public void findById() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();

        bookmarkService.save(bookmark1);
        final Optional<Bookmark> bookmarkOptional = bookmarkService.findById(bookmark1.getId());

        assertThat(bookmarkOptional.isPresent()).isTrue();
        assertThat(bookmarkOptional.get()).isEqualTo(bookmark1);
    }

    @Test
    public void findAllBookmarks() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));
        Collection<Bookmark> bookmarks = bookmarkService.findAll();

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1, bookmark2);
    }

    @Test
    public void findAllBookmarksForUser() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2));
        Collection<Bookmark> bookmarks = bookmarkService.findByOwner("owner1");

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test
    public void findAllTags() throws Exception {
        Bookmark bookmark1 =
                aBookmark().withOwner("owner").withUrl("url1").withTitle("title1").addTag("tag1").addTag("common")
                        .build();
        Bookmark bookmark2 =
                aBookmark().withOwner("owner").withUrl("url2").withTitle("title2").addTag("tag2").addTag("common")
                        .build();
        Bookmark bookmark3 = aBookmark().withOwner("owner").withUrl("url3").withTitle("title3").addTag("tag3").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));
        Collection<String> tags = bookmarkService.findAllTags();

        assertThat(tags).containsExactlyInAnyOrder("tag1", "tag2", "tag3", "common");
    }

    @Test
    public void findAllTagsbyOwner() throws Exception {
        Bookmark bookmark1 =
                aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").addTag("common")
                        .build();
        Bookmark bookmark2 =
                aBookmark().withOwner("owner1").withUrl("url2").withTitle("title2").addTag("tag2").addTag("common")
                        .build();
        Bookmark bookmark3 = aBookmark().withOwner("owner2").withUrl("url3").withTitle("title3").addTag("tag3").build();

        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));
        Collection<String> tags = bookmarkService.findAllTagsByOwner("owner1");

        assertThat(tags).containsExactlyInAnyOrder("tag1", "tag2", "common");
    }

    @Test
    public void updateBookmark() throws Exception {
        Bookmark bookmark = aBookmark().withOwner("owner").withUrl("url0").withTitle("title0").addTag("tag0").build();

        bookmarkService.save(bookmark);
        bookmark.setTitle("title1");
        bookmark.clearTags();
        bookmark.addTag("tag1");
        String id = bookmark.getId();

        bookmarkService.save(bookmark);
        Optional<Bookmark> bookmarkOpt = bookmarkService.getBookmarkById(id);

        assertThat(bookmarkOpt.isPresent()).isTrue();
        //noinspection OptionalGetWithoutIsPresent
        bookmark = bookmarkOpt.get();
        assertThat(bookmark.getId()).isEqualTo(id);
        assertThat(bookmark.getUrl()).isEqualTo("url0");
        assertThat(bookmark.getTitle()).isEqualTo("title1");
        assertThat(bookmark.getTags()).containsExactlyInAnyOrder("tag1");

        assertThat(bookmarkService.findAll()).hasSize(1);
        assertThat(bookmarkService.findAllTags()).hasSize(1);
    }

    @Test
    public void findBookmarkBySearchInTitle() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("Hello world").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("world wide web").build();
        Bookmark bookmark3 = aBookmark().withOwner("owner3").withUrl("url3").withTitle("say hello").build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));

        Collection<Bookmark> bookmarks = bookmarkService.findByTitle("hello");

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1, bookmark3);
    }

    @Test
    public void findBookmarkBySearchInTitleForOwner() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("Hello world").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("world wide web").build();
        Bookmark bookmark3 = aBookmark().withOwner("owner3").withUrl("url3").withTitle("say hello").build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));

        Collection<Bookmark> bookmarks = bookmarkService.findByOwnerAndTitle("owner1", "hello");

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test
    public void findBookmarksWithTagsAnd() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner").withUrl("url1").withTitle("title1").addTag("tag1").addTag
                ("common").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner").withUrl("url2").withTitle("title2").addTag("tag2").addTag
                ("common").build();
        Bookmark bookmark3 = aBookmark().withOwner("owner").withUrl("url3").withTitle("title3").addTag("tag3").build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));

        Collection<Bookmark> bookmarks = bookmarkService.findByTags(Arrays.asList("tag2", "common"));

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark2);
    }

    @Test
    public void findBookmarksWithOwnerAndTagsAnd() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").addTag
                ("common").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner1").withUrl("url2").withTitle("title2").addTag("tag2").addTag
                ("common").build();
        Bookmark bookmark3 = aBookmark().withOwner("owner1").withUrl("url3").withTitle("title3").addTag("tag3").build();
        Bookmark bookmark4 = aBookmark().withOwner("owner2").withUrl("url1").withTitle("title1").addTag("tag1").addTag
                ("common").build();
        Bookmark bookmark5 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").addTag
                ("common").build();
        Bookmark bookmark6 = aBookmark().withOwner("owner2").withUrl("url3").withTitle("title3").addTag("tag3").build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3, bookmark4, bookmark5, bookmark6));

        Collection<Bookmark> bookmarks = bookmarkService.findByOwnerAndTags("owner1", Arrays.asList("tag2", "common"));
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark2);

        bookmarks = bookmarkService.findByOwnerAndTags("owner2", Arrays.asList("tag2", "common"));
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark5);
    }

    @Test
    public void findBookmarksBySearchInTitleAndTags() throws Exception {
        Bookmark bookmark1 =
                aBookmark().withOwner("owner").withUrl("url1").withTitle("Hello world").addTag("tag1").build();
        Bookmark bookmark2 =
                aBookmark().withOwner("owner").withUrl("url2").withTitle("world wide web").addTag("tag1").addTag("tag2")
                        .build();
        Bookmark bookmark3 = aBookmark().withOwner("owner").withUrl("url3").withTitle("say hello").addTag("tag3")
                .build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3));

        Collection<Bookmark> bookmarks =
                bookmarkService.findByTitleAndTags("hello", Collections.singletonList("tag1"));

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test
    public void findBookmarksWithOwnerBySearchInTitleAndTags() throws Exception {
        Bookmark bookmark1 =
                aBookmark().withOwner("owner").withUrl("url1").withTitle("Hello world").addTag("tag1").build();
        Bookmark bookmark2 =
                aBookmark().withOwner("owner").withUrl("url2").withTitle("world wide web").addTag("tag1").addTag("tag2")
                        .build();
        Bookmark bookmark3 = aBookmark().withOwner("owner").withUrl("url3").withTitle("say hello").addTag("tag3")
                .build();
        Bookmark bookmark4 =
                aBookmark().withOwner("owner2").withUrl("url1").withTitle("Hello world").addTag("tag1").build();
        Bookmark bookmark5 =
                aBookmark().withOwner("owner2").withUrl("url2").withTitle("world wide web").addTag("tag1").addTag
                        ("tag2")
                        .build();
        Bookmark bookmark6 = aBookmark().withOwner("owner2").withUrl("url3").withTitle("say hello").addTag("tag3")
                .build();
        bookmarkService.save(Arrays.asList(bookmark1, bookmark2, bookmark3, bookmark4, bookmark5, bookmark6));

        Collection<Bookmark> bookmarks =
                bookmarkService.findByOwnerAndTitleAndTags("owner", "hello", Collections.singletonList("tag1"));

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

}
