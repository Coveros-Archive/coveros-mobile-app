package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.SparseArray;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by SRynestad on 7/25/2017.
 */

public class BookmarksInstrumentedTest {
    private Bookmarks instance;
    private BlogPostReadActivity blogPostReadActivity;
    private String fakeBookmarkFile = "fakeBookmarkFile.xml";
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
        instance = new Bookmarks(fakeBookmarkFile);;
    }
    @After
    public void clean(File fakeBookmarkFile) {
        fakeBookmarkFile.delete();
    }
    @Test
    public void loadExistingBookmarks_withAddedBookmarks(){
        Bookmarks bookmarks = new Bookmarks(fakeBookmarkFile);
        bookmarks.addBookmark(blogPostReadActivity, 4785);
        bookmarks.addBookmark(blogPostReadActivity, 5897);
        bookmarks.addBookmark(blogPostReadActivity, 5698);
        boolean fileLoaded = instance.loadExistingBookmarks(blogPostReadActivity);
        assertThat(fileLoaded, is(true));
    }
    @Test
    public void loadExistingBookmarks_withoutAddedBookmarks(){
        Bookmarks bookmarks = new Bookmarks("noBookmark");
        boolean fileLoaded = instance.loadExistingBookmarks(blogPostReadActivity);
        assertThat(fileLoaded, is(false));
    }
    @Test
    public void checkIfBookmarkIdWasAdded(){
        Bookmarks bookmarks = new Bookmarks(fakeBookmarkFile);
        bookmarks.addBookmark(blogPostReadActivity, 4466);
        bookmarks.addBookmark(blogPostReadActivity,5071);
        boolean bookmarkAdded = instance.addBookmark(blogPostReadActivity, 5071);
        assertThat(bookmarkAdded, is(true));

    }
    @Test
    public void checkIfBookmarkIdWasRemoved(){
        Bookmarks bookmarks = new Bookmarks(fakeBookmarkFile);
        bookmarks.addBookmark(blogPostReadActivity, 4978);
        bookmarks.addBookmark(blogPostReadActivity, 3147);
        bookmarks.addBookmark(blogPostReadActivity, 6464);
        boolean bookmarkRemoved = instance.removeBookmark(blogPostReadActivity, 3147);
        assertThat(bookmarkRemoved, is(true));
    }

}
