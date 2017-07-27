package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class BlogPostsListAdapterInstrumentedTest {
    private static BlogPostsListAdapter blogPostsListAdapter;
    private static BlogPostsListActivity blogPostsListActivity;

    @Rule
    public ActivityTestRule<BlogPostsListActivity> blogPostsListActivityRule = new ActivityTestRule<BlogPostsListActivity>(BlogPostsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;Cats are objectively the worst animal.\"}, \"title\": {\"rendered\": \"&#8220;My Disdain for the Feline Race--an Autobiography\"}}", JsonObject.class);
            SparseArray authors = new SparseArray();
            authors.append(14, "Ryan Kenney");
            BlogPost blogPost = new BlogPost(blogJson, authors);

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
    public void setUp() {

        blogPostsListActivity = blogPostsListActivityRule.getActivity();

        JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;Cats are objectively the worst animal.\"}, \"title\": {\"rendered\": \"&#8220;My Disdain for the Feline Race--an Autobiography\"}}", JsonObject.class);
        SparseArray authors = new SparseArray();
        authors.append(14, "Ryan Kenney");
        BlogPost blogPost = new BlogPost(blogJson, authors);

        List<BlogPost> blogPosts = new ArrayList<>();
        blogPosts.add(blogPost);
        blogPostsListAdapter = new BlogPostsListAdapter(blogPostsListActivity, R.layout.post_list_text, blogPosts);

    }

    @Test
    public void getItem_checkTitle() {
        final String expectedTitle = "\u201CMy Disdain for the Feline Race--an Autobiography";
        String actualTitle = ((BlogPost) blogPostsListAdapter.getItem(0)).getTitle();
        assertThat(actualTitle, equalTo(expectedTitle));
    }

    @Test
    public void getItem_checkId() {
        final int expectedId = 0;
        int actualId = (int) blogPostsListAdapter.getItemId(0);
        assertThat(actualId, equalTo(expectedId));
    }

    @Test
    public void getCount_withOneItem() {
        final int expectedCount = 1;
        int actualCount = blogPostsListAdapter.getCount();
        assertThat(actualCount, equalTo(expectedCount));
    }

    @Test
    public void getView_withNullConvertView() {
        final String expectedTitle = "\u201CMy Disdain for the Feline Race--an Autobiography";
        final String expectedAuthorDate = "Ryan Kenney\nJun 27, 2017";

        View view = blogPostsListAdapter.getView(0, null, blogPostsListActivity.getBlogPostsListView());

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView authorDate = (TextView) view.findViewById(R.id.author_date);

        assertThat(view, is(notNullValue()));
        assertThat(title, is(notNullValue()));
        assertThat(authorDate, is(notNullValue()));

        String actualTitle = title.getText().toString();
        assertThat(actualTitle, equalTo(expectedTitle));

        String actualAuthorDate = authorDate.getText().toString();
        assertThat(actualAuthorDate, equalTo(expectedAuthorDate));

    }

    @Test
    public void getView_withNonNullConvertView() {
        final String expectedTitle = "\u201CMy Disdain for the Feline Race--an Autobiography";
        final String expectedAuthorDate = "Ryan Kenney\nJun 27, 2017";

        ViewGroup convertView = new LinearLayout(blogPostsListActivity);
        BlogPostsListAdapter.BlogPostHolder blogPostHolder = new BlogPostsListAdapter.BlogPostHolder();
        blogPostHolder.title = new TextView(blogPostsListActivity);
        blogPostHolder.authorDate = new TextView(blogPostsListActivity);
        convertView.setTag(blogPostHolder);

        View view = blogPostsListAdapter.getView(0, convertView, blogPostsListActivity.getBlogPostsListView());

        String actualTitle = blogPostsListAdapter.getBlogPostHolder().title.getText().toString();
        assertThat(actualTitle, equalTo(expectedTitle));

        String actualAuthorDate = blogPostsListAdapter.getBlogPostHolder().authorDate.getText().toString();
        assertThat(actualAuthorDate, equalTo(expectedAuthorDate));
    }
}
