package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.SparseArray;

import com.coveros.coverosmobileapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlogPostReadActivityInstrumentedTest {

    private static BlogPostReadActivity blogPostReadActivity;

    private static final String EXPECTED_TITLE = "\u201CMy Disdain for the Feline Race--an Autobiography";

    @Rule
    public ActivityTestRule<BlogPostReadActivity> blogPostReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;Cats are objectively the worst animal.\"}, \"title\": {\"rendered\": \"&#8220;My Disdain for the Feline Race--an Autobiography\"}}", JsonObject.class);
            SparseArray authors = new SparseArray();
            authors.append(14, "Ryan Kenney");
            BlogPost blogPost = new BlogPost(blogJson, authors);

            ArrayList<String> blogPostData = new ArrayList<>();
            blogPostData.add(Integer.toString(blogPost.getId()));
            blogPostData.add(blogPost.getTitle());
            blogPostData.add(blogPost.getContent());
            intent.putStringArrayListExtra("postData", blogPostData);
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostReadActivity = blogPostReadActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkWebViewContentIsShown() {
        boolean webViewContentIsShown = blogPostReadActivity.findViewById(R.id.content).isShown();
        assertTrue("Content should be displayed", webViewContentIsShown);
    }

    @Test
    public void onCreate_checkViewCommentsButtonIsShown() {
        boolean viewCommentsButtonIsShown = blogPostReadActivity.findViewById(R.id.view_comments).isShown();
        assertTrue("Content should be displayed", viewCommentsButtonIsShown);
    }


}
