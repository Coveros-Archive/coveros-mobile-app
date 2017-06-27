package com.coveros.coverosmobileapp.blogpost;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static junit.framework.Assert.assertEquals;

/**
 * @author Maria Kim
 */
public class BlogPostTest {

    private BlogPost blogPost = new BlogPost(1234, "Ryan Kenney", "1996-02-27T00:00:00", "content&#8212;content", "&#8220;BlogPost");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void formatDate_withValidInput() throws ParseException {
        assertEquals("Feb 27, 1996", blogPost.formatDate("1996-02-27T00:00:00"));
    }

    @Test(expected = ParseException.class)
    public void formatDate_withInvalidInput() throws ParseException {
        blogPost.formatDate("02/27/96");
    }

    @Test
    public void unescapeTitle_withUnicodeSymbol() {
        assertEquals("\u201CPost", blogPost.getTitle());
    }

    @Test
    public void unescapeContent_withUnicodeSymbol() {
        assertEquals("content\u2014content", blogPost.getContent());
    }


}
