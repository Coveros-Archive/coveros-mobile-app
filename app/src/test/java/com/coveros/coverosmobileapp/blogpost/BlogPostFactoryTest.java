package com.coveros.coverosmobileapp.blogpost;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brian on 7/13/17.
 */
public class BlogPostFactoryTest {

    @Test
    public void testCreateBlogPostWithInvalidDateThrowsException() {
        int index = new Random().nextInt();
        String author = "Ryan Kenney";
        JsonObject blogJson = createBlogPostWithInvalidDate();
        SparseArray authors = mock(SparseArray.class);
        //when(authors.get(index)).thenReturn(author);
        try {
            new BlogPostFactory().createBlogPost(blogJson, authors);
            fail("Blog Post JSON with invalid date did not throw an Exception");
        } catch (InvalidBlogPostJsonDateException e) {
            // If the test gets here it is a success
        }
    }

    @Test
    public void testCreateBlogPostReturnsExpectedValues() {
        int id = new Random().nextInt();
        Date date = new Date(System.currentTimeMillis());
        String author = "Ryan";
        String content = "Content";
        String title = "title";

        //String expectedAuthorDate = author + "\n" + new SimpleDateFormat(BlogPostFactory.WORDPRESS_BLOGPOST_DATE_FORMAT).format(date);

        BlogPost blogPost = new BlogPost(id, date, content, title, author);
        assertThat(blogPost.getId(), equalTo(id));
        assertThat(blogPost.getContent(), equalTo(content));
        assertThat(blogPost.getTitle(), equalTo(title));
        assertThat(blogPost.getDate(), equalTo(date));
        assertThat(blogPost.getAuthor(), equalTo(author));

        //assertThat(blogPost.getAuthorDate(), equalTo(expectedAuthorDate));
    }

    @Test
    public void testDateConstants() {
        assertThat("yyyy-MM-dd'T'HH:mm:ss", equalTo(BlogPostFactory.WORDPRESS_BLOGPOST_DATE_FORMAT));
        assertThat("MMM d, yyyy", equalTo(BlogPostHtmlDecorator.HTML_DATE_FORMAT_STRING));
    }

    @Test
    public void testCreateBlogPost () {
        int authorId = new Random().nextInt();
        String author = "Ryan Kenney";
        String expectedContent = "content";
        int id = 1234;
        String title = "“BlogPost";
        String expectedAuthorDate = author + "\n" + "Fri Feb 03 00:00:00 EST 1911";
        JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": " + authorId + ", \"date\": \"1911-02-03T00:00:00\", \"content\": {\"rendered\": \"" + expectedContent + "\"}, \"title\": {\"rendered\": \"&#8220;BlogPost\"}}", JsonObject.class);
        SparseArray authors = mock(SparseArray.class);
        when(authors.get(authorId)).thenReturn(author);
        BlogPost blogPost = new BlogPostFactory().createBlogPost(blogJson, authors);
        assertThat(blogPost.getId(), equalTo(id));
        assertThat(blogPost.getContent(), equalTo(expectedContent));
        assertThat(blogPost.getTitle(), equalTo(title));
        assertThat(blogPost.getAuthorDate(), equalTo(expectedAuthorDate));

    }
    private JsonObject createBlogPostWithInvalidDate() {
        return new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"19102-03T00:00:00\", \"content\": {\"rendered\": \"<p>I like to make unfunny puns.&#8212;</p>\"}, \"title\": {\"rendered\": \"&#8220;BlogPost\"}}", JsonObject.class);
    }


    /*
    1. Title
        a. Proper field retrieved
        b. unescaped properly
    2. Author
    3. Content
    4. Date
    5. ID
    6. AuthorDate
     */
}
