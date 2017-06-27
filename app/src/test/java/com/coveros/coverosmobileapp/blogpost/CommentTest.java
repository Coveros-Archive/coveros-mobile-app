package com.coveros.coverosmobileapp.blogpost;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;

/**
 * @author Maria Kim
 */

public class CommentTest {

    JsonObject outerJson = new JsonObject();
    Comment comment;

    @Before
    public void setUp() {
        outerJson.addProperty("author_name", "&#8212;Ryan");
        outerJson.addProperty("date", "2017-06-27T10:23:18");

        JsonObject innerJson = new JsonObject();
        innerJson.addProperty("rendered", "I like to make typos in my published blog posts.&#8221;");
        outerJson.add("content", innerJson);
        comment = new Comment(outerJson);
    }

    @Test
    public void unescapeAuthor_withUnicodeSymbol() {
        assertEquals("\u2014Ryan", comment.getAuthor());
    }

    @Test
    public void unescapeContent_withUnicodeSymbol() {
        assertEquals("I like to make typos in my published blog posts.\u201D", comment.getContent());
    }

    @Test
    public void formatDate_withValidString() {
        assertEquals("Jun 27, 2017", comment.getDate());
    }

}
