package com.sothawo.taboo3.data;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
public class BookmarkEditTest {
    @Test
    public void getTagsAsString() throws Exception {
        BookmarkEdit bookmarkEdit = new BookmarkEdit();
        bookmarkEdit.addTag("tag1");
        bookmarkEdit.addTag("tag3");
        bookmarkEdit.addTag("tag2");

        assertThat(bookmarkEdit.getTagsAsString()).isEqualToIgnoringCase("tag1, tag2, tag3");
    }

    @Test
    public void setTagsAsString() throws Exception {
        BookmarkEdit bookmarkEdit = new BookmarkEdit();
        bookmarkEdit.setTagsAsString("tag1, tag2; tag3");

        assertThat(bookmarkEdit.getTags()).containsExactlyInAnyOrder("tag1", "tag2", "tag3");
    }
}
