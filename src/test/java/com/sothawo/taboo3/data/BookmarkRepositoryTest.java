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
import java.util.List;

import static com.sothawo.taboo3.data.BookmarkBuilder.aBookmark;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class BookmarkRepositoryTest {

    @Autowired
    BookmarkRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
    }


    @Test
    public void insertAndRetrieve() throws Exception {

        Bookmark bookmark = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        repository.save(bookmark);

        Collection<Bookmark> bookmarks = repository.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark);
    }

    @Test(expected = AlreadyExistsException.class)
    public void insertingWithSameIdOverwrites() throws Exception {
        Bookmark bookmark1 = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTitle("this is the first entry")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        Bookmark bookmark2 = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTitle("this is the second entry")
                .withTags(Arrays.asList("it rocks", "mj"))
                .build();

        repository.save(Arrays.asList(bookmark1, bookmark2));

        fail("expected AlreadyExistsException");
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

        repository.save(Arrays.asList(bookmark1, bookmark2));

        assertThat(repository.findByOwner("peter")).containsOnly(bookmark1);
        assertThat(repository.findByOwner("other")).containsOnly(bookmark2);
    }


    @Test
    public void initiallyEmpty() throws Exception {
        assertThat(repository.findAll()).hasSize(0);
        assertThat(repository.findAllTags()).hasSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithoutUrl() throws Exception {
        repository.save(aBookmark().withOwner("owner").withTitle("title").build());
        fail("excpected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithoutOwner() throws Exception {
        repository.save(aBookmark().withUrl("url").withTitle("title").build());
        fail("excpected IllegalArgumentException");
    }

    @Test
    public void deleteExistingBookmark() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner").withUrl("url2").withTitle("title2").addTag("tag2").build();

        repository.save(Arrays.asList(bookmark1, bookmark2));
        repository.deleteBookmark(bookmark2);

        final Collection<Bookmark> bookmarks = repository.findAll();
        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistingBookmark() throws Exception {
        repository.deleteBookmark(aBookmark().withOwner("owner").withUrl("url").build());
        fail("expected NotFoundException");
    }


    @Test
    public void findAllBookmarks() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").build();

        repository.save(Arrays.asList(bookmark1, bookmark2));
        Collection<Bookmark> bookmarks = repository.findAll();

        assertThat(bookmarks).containsExactlyInAnyOrder(bookmark1, bookmark2);
    }

    @Test
    public void findAllBookmarksForUser() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("owner1").withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withOwner("owner2").withUrl("url2").withTitle("title2").addTag("tag2").build();

        repository.save(Arrays.asList(bookmark1, bookmark2));
        Collection<Bookmark> bookmarks = repository.findByOwner("owner1");

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

        repository.save(Arrays.asList(bookmark1, bookmark2, bookmark3));
        Collection<String> tags = repository.findAllTags();

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

        repository.save(Arrays.asList(bookmark1, bookmark2, bookmark3));
        Collection<String> tags = repository.findAllTagsByOwner("owner1");

        assertThat(tags).containsExactlyInAnyOrder("tag1", "tag2", "common");
    }

    //############ rewrite the following tests
/*
    @Test
    public void findBookmarkBySearchInTitle() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("Hello world").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("world wide web").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("say hello").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.getBookmarksWithSearch("hello");

        assertThat(bookmarks, hasSize(2));
        assertThat(bookmarks, hasItems(bookmark1, bookmark3));
    }

    @Test
    public void findBookmarksBySearchInTitleAndTags() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("Hello world").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("world wide web").addTag("tag1").addTag("tag2")
                .build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("say hello").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.getBookmarksWithTagsAndSearch(Collections.singletonList("tag1"), true, "hello");

        assertThat(bookmarks, hasSize(1));
        assertThat(bookmarks, hasItems(bookmark1));
    }

    @Test
    public void findBookmarksWithTagsAnd() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("title3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.getBookmarksWithTags(Arrays.asList("tag2", "common"), true);

        assertThat(bookmarks, hasSize(1));
        assertThat(bookmarks, hasItem(bookmark2));
    }

    @Test
    public void findBookmarksWithTagsOr() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("title3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.getBookmarksWithTags(Arrays.asList("tag2", "common"), false);

        assertThat(bookmarks, hasSize(2));
        assertThat(bookmarks, hasItems(bookmark1, bookmark2));
    }


    @Test
    public void findTagsWhenNoTagsExistYieldsEmptyList() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url1").withTitle("title1").build();
        repository.createBookmark(bookmark);

        Collection<String> tags = repository.getAllTags();

        assertThat(tags, hasSize(0));
    }

    @Test
    public void updateBookmark() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url0").withTitle("title0").addTag("tag0").build();

        bookmark = repository.createBookmark(bookmark);
        bookmark.setUrl("url1");
        bookmark.setTitle("title1");
        bookmark.clearTags();
        bookmark.addTag("tag1");
        String id = bookmark.getId();

        repository.updateBookmark(bookmark);
        bookmark = repository.getBookmarkById(id);

        assertThat(bookmark.getId(), is(id));
        assertThat(bookmark.getUrl(), is("url1"));
        assertThat(bookmark.getTitle(), is("title1"));
        assertThat(bookmark.getTags(), hasSize(1));
        assertThat(bookmark.getTags(), hasItem("tag1"));

        assertThat(repository.getAllBookmarks(), hasSize(1));
        assertThat(repository.getAllTags(), hasSize(1));
    }

    @Test(expected = AlreadyExistsException.class)
    public void updateBookmarkToExistingUrl() throws Exception {
        Bookmark bookmark = repository.createBookmark(aBookmark().withUrl("url0").withTitle("title0").addTag("tag0")
                .build());
        repository.createBookmark(aBookmark().withUrl("url1").withTitle("title1").addTag("tag1")
                .build());
        bookmark.setUrl("url1");

        repository.updateBookmark(bookmark);

        fail("AlreadyExistsException expected");
    }

    @Test(expected = NotFoundException.class)
    public void updateBookmarkWithNotExistingId() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url0").withTitle("title0").addTag("tag0").build();
        bookmark.setId("NotExistingId");

        repository.updateBookmark(bookmark);

        fail("NotFoundException expected");
    }

    @Test(expected = NullPointerException.class)
    public void updateBookmarkWithoutData() throws Exception {
        repository.updateBookmark(null);

        fail("NullPointerException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookmarkWithoutId() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url0").withTitle("title0").addTag("tag0").build();

        repository.updateBookmark(bookmark);

        fail("IllegalArgumentException expected");
    }

*/
}
