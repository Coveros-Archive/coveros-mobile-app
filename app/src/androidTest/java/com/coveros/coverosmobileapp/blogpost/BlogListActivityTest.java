package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * @author Maria Kim
 */

public class BlogListActivityTest {

    BlogListActivity blogListActivity = new BlogListActivity();

    @Test
    public void createTextViewLabel_forComments() {
        TextView commentsLabel = blogListActivity.createTextViewLabel(blogListActivity, "Comments");
        assertEquals("Label should read \"Comments\"", "Comments", commentsLabel.getText());
        assertEquals("Text sizes should match.", blogListActivity.getTextViewTextSize(), commentsLabel.getTextSize());
        assertEquals("Padding bottom should match.", blogListActivity.getTextViewPaddingBottom(), commentsLabel.getPaddingBottom());
    }

//    @Test
//    public void createErrorMessage_checkAlertDialogNotNull() {
//        blogListActivity.errorMessage = blogListActivity.createErrorMessage(mock(Context.class));
//        assertNotNull("errorMessage should not be null.", blogListActivity.errorMessage);
//    }
}
