package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.coveros.coverosmobileapp.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author Maria Kim
 */

public class CommentFormActivityInstrumentedTest {

    private static final String POST_ID = "0";

    @Rule
    public ActivityTestRule<CommentFormActivity> commentFormActivity = new ActivityTestRule<CommentFormActivity>(CommentFormActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", POST_ID);
            return intent;
        }
    };

    @Test
    public void onCreate_checkFormLabelIsShown() {
        assertTrue("Comment form label should be displayed", commentFormActivity.getActivity().findViewById(R.id.comment_form_label).isShown());
    }

    @Test
    public void onCreate_checkNameEditTextIsShown() {
        assertTrue("Enter name EditText should be displayed", commentFormActivity.getActivity().findViewById(R.id.enter_name).isShown());
    }
    @Test
    public void onCreate_checkEmailEditTextIsShown() {
        assertTrue("Enter email EditText should be displayed", commentFormActivity.getActivity().findViewById(R.id.enter_email).isShown());
    }
    @Test
    public void onCreate_checkMessageEditTextIsShown() {
        assertTrue("Enter message EditText should be displayed", commentFormActivity.getActivity().findViewById(R.id.enter_message).isShown());
    }
}
