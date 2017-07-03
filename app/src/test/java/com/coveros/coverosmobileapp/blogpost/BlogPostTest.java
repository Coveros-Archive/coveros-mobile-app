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

    private static final int EXPECTED_ID = 1234;
    private static final String EXPECTED_DATE = "Feb 3, 1911";
    private static final String EXPECTED_CONTENT = "<h3>\u201CBlogPost</h3><h4>Ryan Kenney</h4><h5>Feb 3, 1911</h5><p>I like to make unfunny puns.\u2014</p>";

    @Before
    public void setUp() {
        outerJson.addProperty("id", "1234");
        outerJson.addProperty("author", "14");
        outerJson.addProperty("date", "1911-02-03T00:00:00");

        JsonObject innerContentJson = new JsonObject();
        innerContentJson.addProperty("rendered", "<p>I like to make unfunny puns.&#8212;</p>");
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
        String actualDate = blogPost.formatDate("1911-02-03T00:00:00");
        assertEquals("Dates should be equal.", EXPECTED_DATE, actualDate);
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
        assertEquals(EXPECTED_CONTENT, blogPost.getContent());
    }

    @Test
    public void getId_withId() {
        int actualId = blogPost.getId();
        assertEquals("ids should be equal", EXPECTED_ID, actualId);
    }


}
