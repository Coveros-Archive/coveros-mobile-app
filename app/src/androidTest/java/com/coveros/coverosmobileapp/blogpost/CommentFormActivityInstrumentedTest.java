package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AlertDialog;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Maria Kim
 */

public class CommentFormActivityInstrumentedTest extends LooperTestSuite {

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
        assertThat(formLabelIsShown, equalTo(true));
    }

    @Test
    public void onCreate_checkNameEditTextIsShown() {
        boolean enterNameIsShown = commentFormActivity.findViewById(R.id.enter_name).isShown();
        assertThat(enterNameIsShown, equalTo(true));
    }

    @Test
    public void onCreate_checkEmailEditTextIsShown() {
        boolean enterEmailIsShown = commentFormActivity.findViewById(R.id.enter_email).isShown();
        assertThat(enterEmailIsShown, equalTo(true));
    }
    @Test
    public void onCreate_checkMessageEditTextIsShown() {
        boolean enterMessageIsShown = commentFormActivity.findViewById(R.id.enter_message).isShown();
        assertThat(enterMessageIsShown, equalTo(true));
    }

    @Test
    public void onClick_checkInputStringsAreReadCorrectly() {
        onView(withId(R.id.enter_name)).perform(ViewActions.typeText(EXPECTED_NAME), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(ViewActions.typeText(EXPECTED_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.enter_message)).perform(ViewActions.typeText(EXPECTED_MESSAGE), closeSoftKeyboard());

        onView(withId(R.id.send_button)).perform(click());

        String actualName = commentFormActivity.getAuthor();
        assertThat(actualName, equalTo(EXPECTED_NAME));

        String actualEmail = commentFormActivity.getEmail();
        assertThat(actualEmail, equalTo(EXPECTED_EMAIL));

        String actualMessage = commentFormActivity.getMessage();
        assertThat(actualMessage, equalTo(EXPECTED_MESSAGE));
    }

    @Test
    public void onClick_withEmptyFields() {
        onView(withId(R.id.send_button)).perform(click());
        boolean emptyFieldsAlertDialogIsShowing = commentFormActivity.getEmptyFieldDialog().isShowing();

        assertThat(emptyFieldsAlertDialogIsShowing, equalTo(true));
    }

    @Test
    public void onClick_withInvalidEmail() {
        final String invalidEmail = "ryane";
        onView(withId(R.id.enter_name)).perform(ViewActions.typeText(EXPECTED_NAME), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(ViewActions.typeText(invalidEmail), closeSoftKeyboard());
        onView(withId(R.id.enter_message)).perform(ViewActions.typeText(EXPECTED_MESSAGE), closeSoftKeyboard());
        onView(withId(R.id.send_button)).perform(click());

        boolean invalidEmailDialogIsShowing = commentFormActivity.getInvalidEmailDialog().isShowing();

        assertThat(invalidEmailDialogIsShowing, equalTo(true));
    }

    @Test
    public void createCommentRequestBody() {
        final String expectedPostId = "0000";
        final String expectedName = "Ryan Kenney";
        final String expectedEmail = "dogluvr@gmail.com";
        final String expectedContent = "I dream about dogs every night.";

        JsonObject body = commentFormActivity.createCommentRequestBody(expectedPostId, expectedName, expectedEmail, expectedContent);

        String actualPostId = body.get("post").getAsString();
        String actualName = body.get("author_name").getAsString();
        String actualEmail = body.get("author_email").getAsString();
        String actualContent = body.get("content").getAsString();

        assertThat(actualPostId, equalTo(expectedPostId));
        assertThat(actualName, equalTo(expectedName));
        assertThat(actualEmail, equalTo(expectedEmail));
        assertThat(actualContent, equalTo(expectedContent));

    }

    @Test
    public void commentRequestOnResponse_checkSuccessDialogIsShowing() {
        onView(withId(R.id.enter_name)).perform(ViewActions.typeText(EXPECTED_NAME), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(ViewActions.typeText(EXPECTED_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.enter_message)).perform(ViewActions.typeText(EXPECTED_MESSAGE), closeSoftKeyboard());
        onView(withId(R.id.send_button)).perform(click());

        commentFormActivity.getCommentRequest().getListener().onResponse(new JsonObject());
        AlertDialog successDialog = commentFormActivity.getSuccessDialog();

        boolean isSuccessDialogShowing = successDialog.isShowing();

        assertThat(isSuccessDialogShowing, equalTo(true));
    }

    @Test
    public void commentRequestOnErrorResponse_checkErrorDialogIsShowing() {
        // generate VolleyError
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        onView(withId(R.id.send_button)).perform(click());

        commentFormActivity.getCommentRequest().getErrorListener().onErrorResponse(volleyError);
        AlertDialog errorDialog = commentFormActivity.getErrorDialog();

        boolean isErrorDialogShowing = errorDialog.isShowing();
        assertThat(isErrorDialogShowing, equalTo(true));
    }


}
