package com.coveros.coverosmobileapp.blogpost;

import android.util.SparseArray;

import com.google.gson.Gson;
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

//    JsonObject outerJson = new JsonObject();
    BlogPost blogPost;

    private static final int EXPECTED_ID = 1234;
    private static final String EXPECTED_DATE = "Feb 3, 1911";
    private static final String EXPECTED_CONTENT = "<h3 style=\"padding-right:35px\">\u201CBlogPost</h3><h4>Ryan Kenney</h4><h5>Feb 3, 1911</h5><p>I like to make unfunny puns.\u2014</p>";

    @Before
    public void setUp() {

        JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"1911-02-03T00:00:00\", \"content\": {\"rendered\": \"<p>I like to make unfunny puns.&#8212;</p>\"}, \"title\": {\"rendered\": \"&#8220;BlogPost\"}}", JsonObject.class);
        SparseArray authors = mock(SparseArray.class);
        when(authors.get(14)).thenReturn("Ryan Kenney");
        blogPost = new BlogPost(blogJson, authors);
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
