package com.coveros.coverosmobileapp.blogpost;

import android.util.SparseArray;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Maria Kim
 */
public class BlogPostTest {

    JsonObject outerJson = new JsonObject();
    BlogPost blogPost;

    @Before
    public void setUp() {
        outerJson.addProperty("id", "1234");
        outerJson.addProperty("author", "14");
        outerJson.addProperty("date", "1911-02-03T00:00:00");

        JsonObject innerContentJson = new JsonObject();
        innerContentJson.addProperty("rendered", "I like to make unfunny puns.&#8212;");
        outerJson.add("content", innerContentJson);
        JsonObject innerTitleJson = new JsonObject();
        innerTitleJson.addProperty("rendered", "&#8220;BlogPost");
        outerJson.add("title", innerTitleJson);

        SparseArray authors = mock(SparseArray.class);
        when(authors.get(14)).thenReturn("Ryan Kenney");
        blogPost = new BlogPost(outerJson, authors);
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void formatDate_withValidInput() throws ParseException {
        assertEquals("Feb 3, 1911", blogPost.formatDate("1911-02-03T00:00:00"));
    }

    @Test(expected = ParseException.class)
    public void formatDate_withInvalidInput() throws ParseException {
        blogPost.formatDate("02/03/91");
    }

    @Test
    public void unescapeTitle_withUnicodeSymbol() {
        assertEquals("\u201CBlogPost", blogPost.getTitle());
    }

    @Test
    public void unescapeContent_withUnicodeSymbol() {
        assertEquals("<h3>\u201CBlogPost</h3><h4>Ryan Kenney</h4><h5>Feb 3, 1911</h5>I like to make unfunny puns.\u2014", blogPost.getContent());
    }


}
