package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.espresso.action.ViewActions;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

    private static CommentFormActivity commentFormActivity;

    private static final String POST_ID = "0";
    private static String EXPECTED_NAME = "Ryan Kenney";
    private static String EXPECTED_EMAIL = "ultimatecatluver@catlovers.com";
    private static String EXPECTED_MESSAGE = "I love cats so much, even though they are jerks. No, especially because they are jerks.";

    @Rule
    public ActivityTestRule<CommentFormActivity> commentFormActivityRule = new ActivityTestRule<CommentFormActivity>(CommentFormActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", POST_ID);
            return intent;
        }
    };

    @Before
    public void setUp() {
        commentFormActivity = commentFormActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkFormLabelIsShown() {
        boolean formLabelIsShown = commentFormActivity.findViewById(R.id.comment_form_label).isShown();
        assertTrue("Comment form label should be displayed", formLabelIsShown);
    }

    @Test
    public void onCreate_checkNameEditTextIsShown() {
        boolean enterNameIsShown = commentFormActivity.findViewById(R.id.enter_name).isShown();
        assertTrue("Enter name EditText should be displayed", enterNameIsShown);
    }
    @Test
    public void onCreate_checkEmailEditTextIsShown() {
        boolean enterEmailIsShown = commentFormActivity.findViewById(R.id.enter_email).isShown();
        assertTrue("Enter email EditText should be displayed", enterEmailIsShown);
    }
    @Test
    public void onCreate_checkMessageEditTextIsShown() {
        boolean enterMessageIsShown = commentFormActivity.findViewById(R.id.enter_message).isShown();
        assertTrue("Enter message EditText should be displayed", enterMessageIsShown);
    }

    @Test
    public void onClick_checkInputStringsAreReadCorrectly() {
        onView(withId(R.id.enter_name)).perform(ViewActions.typeText(EXPECTED_NAME), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(ViewActions.typeText(EXPECTED_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.enter_message)).perform(ViewActions.typeText(EXPECTED_MESSAGE), closeSoftKeyboard());

        onView(withId(R.id.send_button)).perform(click());

        String actualName = commentFormActivity.getAuthor();
        assertEquals("Names should match.", EXPECTED_NAME, actualName);

        String actualEmail = commentFormActivity.getEmail();
        assertEquals("Emails should match.", EXPECTED_EMAIL, actualEmail);

        String actualMessage = commentFormActivity.getMessage();
        assertEquals("Messages should match.", EXPECTED_MESSAGE, actualMessage);
    }
}
