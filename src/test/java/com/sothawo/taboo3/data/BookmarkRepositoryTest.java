package com.sothawo.taboo3.data;

import com.sothawo.taboo3.Taboo3jApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static com.sothawo.taboo3.data.BookmarkBuilder.aBookmark;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Taboo3jApplication.class)
@DirtiesContext
public class BookmarkRepositoryTest {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Before
    public void setup() {
        bookmarkRepository.deleteAll();
    }

    @Test
    public void insertAndRetrieve() throws Exception {

        Bookmark bookmark = aBookmark()
                .withOwner("peter")
                .withUrl("https://www.sothawo.com")
                .withTags(Arrays.asList("cool", "important"))
                .build();

        bookmarkRepository.save(bookmark);

        final List<Bookmark> petersBookmarks = bookmarkRepository.findByOwner("peter");
        assertThat(petersBookmarks).isNotNull().hasSize(1);
        final Bookmark bookmark1 = petersBookmarks.get(0);
        assertThat(bookmark.getId()).isEqualTo(bookmark1.getId());
        assertThat(bookmark.getTitle()).isEqualTo(bookmark1.getTitle());
    }

    @Test
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

        bookmarkRepository.save(Arrays.asList(bookmark1, bookmark2));

        final List<Bookmark> petersBookmarks = bookmarkRepository.findByOwner("peter");
        assertThat(petersBookmarks).isNotNull().hasSize(1);
        final Bookmark bookmark = petersBookmarks.get(0);
        assertThat(bookmark.getId()).isEqualTo(bookmark2.getId());
        assertThat(bookmark.getTitle()).isEqualTo(bookmark2.getTitle());
    }
}
