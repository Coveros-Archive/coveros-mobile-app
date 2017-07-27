package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by SRynestad on 7/25/2017.
 */

public class BookmarksInstrumentedTest {
    private Bookmarks instance;
    private BlogPostReadActivity blogPostReadActivity;
    private String fakeBookmarkFile = "fakeBookmarkFile.xml";
    //instantiate
    private BlogPost blogPost;
    @Rule
    public ActivityTestRule<BlogPostReadActivity> blogPostsReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"1911-02-03T00:00:00\", \"content\": {\"rendered\": \"<p>I like to make unfunny puns.&#8212;</p>\"}, \"title\": {\"rendered\": \"&#8220;BlogPost\"}}", JsonObject.class);
            SparseArray authors = new SparseArray();
            authors.append(14, "Ryan Kenney");
            blogPost = new BlogPost(blogJson, authors);
            ArrayList<String> blogPostData = new ArrayList<>();
            blogPostData.add(String.valueOf(blogPost.getId()));
            blogPostData.add(blogPost.getTitle());
            blogPostData.add(blogPost.getContent());
            Intent intent = new Intent();
            intent.putStringArrayListExtra("blogPostData", blogPostData);
            return intent;
        }
    };
    @Before
    public void setup(){
        blogPostReadActivity = blogPostsReadActivityRule.getActivity();
        instance = new Bookmarks(fakeBookmarkFile);
        File file = mock(File.class);

        List<String> fileNames = new ArrayList<>();
        fileNames.add(fakeBookmarkFile);
        String[] fileNamesStringArr = fileNames.toArray(new String[0]);
        String[] fileName = {"file1", "file2", "file3"};
    }
    @After
    public void clean(File fakeBookmarkFile) {
        fakeBookmarkFile.delete();
    }
    @Test
    public void checkIfFileWasLoaded(){

        boolean fileLoaded = instance.loadExistingBookmarks(blogPostReadActivity);
        assertTrue("existing file should be loaded", fileLoaded);
    }
    @Test
    public void checkIfBookmarkIdWasAdded(){
        boolean bookmarkAdded = instance.addBookmark(blogPostReadActivity, blogPost.getId());
        assertTrue("bookmark should be added", bookmarkAdded);

    }
    @Test
    public void checkIfBookmarkIdWasRemoved(){
        boolean bookmarkRemoved = instance.removeBookmark(blogPostReadActivity, blogPost.getId());
        assertTrue("bookmark should be removed", bookmarkRemoved);
    }

}
