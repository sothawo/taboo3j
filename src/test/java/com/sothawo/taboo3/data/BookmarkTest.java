package com.sothawo.taboo3.data;

import org.junit.Test;

import java.util.Collection;

import static com.sothawo.taboo3.data.BookmarkBuilder.aBookmark;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class BookmarkTest {
    @Test
    public void builderSetsAllFields() throws Exception {
        Bookmark bookmark = aBookmark()
                .withOwner("owner")
                .withUrl("url").withTitle("title").addTag("tag1").addTag("tag2").build();
        Collection<String> tags = bookmark.getTags();

        assertThat(bookmark.getId()).isEqualTo("owner-url");
        assertThat(bookmark.getOwner()).isEqualTo("owner");
        assertThat(bookmark.getTitle()).isEqualTo("title");
        assertThat(bookmark.getUrl()).isEqualTo("url");
        assertThat(tags).containsExactly("tag1", "tag2");
    }

    @Test
    public void twoBookmarksWithSameOwnerAndUrlAreEqual() throws Exception {
        Bookmark bookmark1 = aBookmark().withOwner("own").withUrl("uurrll").build();
        Bookmark bookmark2 = aBookmark().withOwner("own").withUrl("uurrll").build();

        assertThat(bookmark1).isNotSameAs(bookmark2);
        assertThat(bookmark1).isEqualTo(bookmark2);
    }

    @Test
    public void tagsAreLowercase() throws Exception {
        Collection<String> tags = aBookmark().withOwner("owner").withUrl("url").addTag("ABC").build().getTags();
        assertThat(tags).containsExactly("abc");
    }

    @Test
    public void ownerIsLowercase() throws Exception {
        final Bookmark bookmark = aBookmark().withOwner("OWNER").withUrl("url").build();

        assertThat(bookmark.getOwner()).isEqualTo("owner");
    }

    @Test
    public void tagsHaveNoDuplicates() throws Exception {
        Collection<String> tags = aBookmark().withOwner("owner").withUrl("url").addTag("ABC").addTag("abc").build()
                .getTags();

        assertThat(tags).containsExactly("abc");
    }
}
