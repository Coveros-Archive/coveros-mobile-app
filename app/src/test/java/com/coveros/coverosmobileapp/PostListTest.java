package com.coveros.coverosmobileapp;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import org.junit.Assert;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Maria Kim on 6/12/2017.
 */

public class PostListTest {

    PostList postList;

    @Before
    public void setUp() {
        postList = new PostList();
    }

    @Test
    public void getPostTitles_withValidResponse() throws Exception {
        String[] expectedTitles = {"How to Write Unit Tests", "“Agile Development”"};
        Gson gson = new Gson();
        String validResponse = "[{\"title\": {\"rendered\": \"How to Write Unit Tests\"}},{\"title\": {\"rendered\": \"&#8220;Agile Development&#8221;\"}}]";
        ArrayList input = (ArrayList) gson.fromJson(validResponse, ArrayList.class);
        Assert.assertArrayEquals("Post titles should be correctly extracted and formatted from JSON response.", expectedTitles, postList.getPostTitles(input));
    }

    @Test
    public void getPostTItles_withInvalidResponse() throws Exception {
        String[] expectedTitles = {"How to Write Unit Tests", "“Agile Development”"};
        Gson gson = new Gson();
        String invalidResponse = "[\"title\": {\"rendered\": \"How to Write Unit Tests\"}},{\"title\": {\"rendered\": \"&#8220;Agile Development&#8221;\"}}]";
        ArrayList input = (ArrayList) gson.fromJson(invalidResponse, ArrayList.class);
        Assert.assertArrayEquals("Post titles should not be correctly extracted and formatted from JSON response because the JSON is in an invalid format.", expectedTitles, postList.getPostTitles(input));

    }


}

//onresponse listener .apply()
