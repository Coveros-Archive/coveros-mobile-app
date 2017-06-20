package com.coveros.coverosmobileapp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.text.ParseException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by maria on 6/20/2017.
 */

public class PostTest {

    Post post = new Post("Post", "1996-02-27T00:00:00", new Author("Ryan Kenney", 14), 1234, "content");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void formatDate_withValidInput() {
        try {
            assertEquals("Feb 27, 1996", post.formatDate("1996-02-27T00:00:00"));
        } catch (ParseException e) {
            System.err.println(e);
        }
    }

    @Test(expected = ParseException.class)
    public void formatDate_withInvalidInput() throws ParseException{
        String parsedDate = post.formatDate("02/27/96");
    }
}
