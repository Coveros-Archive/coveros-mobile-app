package com.coveros.coverosmobileapp.blogpost;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class CommentsListAdapterInstrumentedTest {

    private static CommentsListAdapter commentsListAdapter;
    private static CommentsListActivity commentsListActivity;

    @Rule
    public ActivityTestRule<CommentsListActivity> commentsListActivityRule = new ActivityTestRule<CommentsListActivity>(CommentsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", "0");
            return intent;
        }
    };

    @Before
    public void setUp() {

        commentsListActivity = commentsListActivityRule.getActivity();
        JsonObject commentJson = new Gson().fromJson("{\"author_name\": \"Ryan Kenney\", \"date\": \"2017-06-27T10:23:18\", \"content\": {\"rendered\": \"&#8220;I made Sadie get the rotisserie chicken.\"}}", JsonObject.class);

        Comment comment = new Comment(commentJson);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        commentsListAdapter = new CommentsListAdapter(commentsListActivity, R.layout.comment_list_text, comments);

    }

    @Test
    public void getItem_checkName() {
        final String expectedName = "Ryan Kenney";
        String actualName = ((Comment) commentsListAdapter.getItem(0)).getAuthor();
        assertThat(actualName, equalTo(expectedName));
    }

    @Test
    public void getItem_checkId() {
        final int expectedId = 0;
        int actualId = (int) commentsListAdapter.getItemId(0);
        assertThat(actualId, equalTo(expectedId));
    }

    @Test
    public void getCount_withOneItem() {
        final int expectedCount = 1;
        int actualCount = commentsListAdapter.getCount();
        assertThat(actualCount, equalTo(expectedCount));
    }

    @Test
    public void getView_withNullConvertView() {
        final String expectedName = "Ryan Kenney";
        final String expectedDate = "Jun 27, 2017";
        final String expectedContent = "\u201CI made Sadie get the rotisserie chicken.";

        View view = commentsListAdapter.getView(0, null, commentsListActivity.getListView());

        TextView name = (TextView) view.findViewById(R.id.comment_name);
        TextView date = (TextView) view.findViewById(R.id.comment_date);
        TextView content = (TextView) view.findViewById(R.id.comment_content);

        assertThat(view, is(notNullValue()));
        assertThat(name, is(notNullValue()));
        assertThat(date, is(notNullValue()));
        assertThat(content, is(notNullValue()));

        String actualName = name.getText().toString();
        assertThat(actualName, equalTo(expectedName));

        String actualDate = date.getText().toString();
        assertThat(actualDate, equalTo(expectedDate));

        String actualContent = content.getText().toString();
        assertThat(actualContent, equalTo(expectedContent));
    }


    @Test
    public void getView_withNonNullConvertView() {
        final String expectedName = "Ryan Kenney";
        final String expectedDate = "Jun 27, 2017";
        final String expectedContent = "\u201CI made Sadie get the rotisserie chicken.";

        ViewGroup convertView = new LinearLayout(commentsListActivity);
        CommentsListAdapter.CommentHolder commentHolder = new CommentsListAdapter.CommentHolder();
        commentHolder.commentName = new TextView(commentsListActivity);
        commentHolder.commentDate = new TextView(commentsListActivity);
        commentHolder.commentContent = new TextView(commentsListActivity);
        convertView.setTag(commentHolder);

        View view = commentsListAdapter.getView(0, convertView, commentsListActivity.getListView());

        String actualName = commentsListAdapter.getCommentHolder().commentName.getText().toString();
        assertThat(actualName, equalTo(expectedName));

        String actualDate = commentsListAdapter.getCommentHolder().commentDate.getText().toString();
        assertThat(actualDate, equalTo(expectedDate));

        String actualContent = commentsListAdapter.getCommentHolder().commentContent.getText().toString();
        assertThat(actualContent, equalTo(expectedContent));
    }



}