package com.coveros.coverosmobileapp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.text.ParseException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by maria on 6/20/2017.
 */

public class PostTest {

    Post post = new Post("&#8220;Post", "1996-02-27T00:00:00", new Author("Ryan Kenney", 14), 1234, "content&#8212;content");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void formatDate_withValidInput() throws ParseException {
        assertEquals("Feb 27, 1996", post.formatDate("1996-02-27T00:00:00"));
    }

    @Test(expected = ParseException.class)
    public void formatDate_withInvalidInput() throws ParseException {
        post.formatDate("02/27/96");
    }

    @Test
    public void unescapeTitle_withUnicodeSymbol() {
        assertEquals("\u201CPost", post.getTitle());
    }

    @Test
    public void unescapeContent_withUnicodeSymbol() {
        assertEquals("content\u2014content", post.getContent());
    }


}
