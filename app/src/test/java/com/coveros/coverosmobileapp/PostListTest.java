package com.coveros.coverosmobileapp;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import org.junit.Assert;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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
        String[] expectedTitles = {"How to Write Unit Tests", "\u201CAgile Development\u201D"};
        Gson gson = new Gson();
        String validResponse = "[{\"title\": {\"rendered\": \"How to Write Unit Tests\"}},{\"title\": {\"rendered\": \"&#8220;Agile Development&#8221;\"}}]";
        String[] input = gson.fromJson(validResponse, String[].class);
        assertArrayEquals("Post titles should be correctly extracted and formatted from JSON response.", expectedTitles, postList.getPostTitles(input));
    }


}

//onresponse listener .apply()
