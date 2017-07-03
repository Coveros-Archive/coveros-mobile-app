package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import com.coveros.coverosmobileapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Maria Kim
 */

public class CommentFormActivityInstrumentedTest {

    private static final String POST_ID = "0";
    private static String NAME = "Ryan Kenney";
    private static String EMAIL = "ultimatecatluver@catlovers.com";
    private static String MESSAGE = "I love cats so much, even though they are jerks. No, especially because they are jerks.";

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

    @Test
    public void onClick_checkInputStringsAreReadCorrectly() {
        onView(withId(R.id.enter_name)).perform(ViewActions.typeText(NAME), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(ViewActions.typeText(EMAIL), closeSoftKeyboard());
        onView(withId(R.id.enter_message)).perform(ViewActions.typeText(MESSAGE), closeSoftKeyboard());

        onView(withId(R.id.send_button)).perform(click());

        assertEquals("Names should match.", NAME, commentFormActivity.getActivity().author);
        assertEquals("Emails should match.", EMAIL, commentFormActivity.getActivity().email);
        assertEquals("Messages should match.", MESSAGE, commentFormActivity.getActivity().message);
    }
}
