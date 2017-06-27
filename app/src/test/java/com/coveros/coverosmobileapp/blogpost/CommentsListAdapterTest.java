package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.test.mock.MockContext;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Maria Kim
 */
public class CommentsListAdapterTest {

    List<Comment> comments;
    @Before
    public void setUp() {
        comments = new ArrayList<>();
        comments.add(new Comment("Ryan Kenney", "Feb 2, 2011", "<p>I made Sadie get the rotisserie chicken.</p>"));
    }

    @Test
    public void createCommentsListAdapter() {
        Context context = new CommentsListActivity();
        CommentsListAdapter commentsListAdapter = new CommentsListAdapter(context, 0, comments);

        // setting up mock view
//        View mConvertView = mock(View.class);
//        CommentsListAdapter.CommentHolder mCommentHolder = mock(CommentsListAdapter.CommentHolder.class);
//        when(mConvertView.getTag()).thenReturn(mCommentHolder);
//        when(mCommentHolder.getCommentName()).thenReturn(mock(TextView.class));
//        when(mCommentHolder.getCommentDate()).thenReturn(mock(TextView.class));
//        when(mCommentHolder.getCommentContent()).thenReturn(mock(TextView.class));

        View view = commentsListAdapter.getView(0, null, new LinearLayout(mock(Context.class)));

        CommentsListAdapter.CommentHolder commentHolder = (CommentsListAdapter.CommentHolder) view.getTag();

        assertEquals("Ryan Kenney", commentHolder.getCommentName().getText());
        assertEquals("Feb 2, 2011", commentHolder.getCommentDate().getText());
        assertEquals("<p>I made Sadie get the rotisserie chicken.</p>", commentHolder.getCommentContent().getText());
    }



}