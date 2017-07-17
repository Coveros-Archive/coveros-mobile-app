package com.coveros.coverosmobileapp.oauth.example;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Maria Kim
 */

public class BlogPostUpdateActivityInstrumentedTest extends LooperTestSuite {

    BlogPostUpdateActivity blogPostUpdateActivity;

    @Rule
    public ActivityTestRule<BlogPostUpdateActivity> blogPostUpdateActivityRule = new ActivityTestRule<BlogPostUpdateActivity>(BlogPostUpdateActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("access", "525");
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostUpdateActivity = blogPostUpdateActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkPostIdEditTextIsShown() {
        boolean postIdEditTextIsShown = blogPostUpdateActivity.findViewById(R.id.enter_post_id).isShown();
        assertThat(postIdEditTextIsShown, equalTo(true));
    }

    @Test
    public void onCreate_checkNewContentEditTextIsShown() {
        boolean newContentEditTextIsShown = blogPostUpdateActivity.findViewById(R.id.enter_new_content).isShown();
        assertThat(newContentEditTextIsShown, equalTo(true));

    }

    @Test
    public void onCreate_checkPostButtonIsShown() {
        boolean postButtonIsShown = blogPostUpdateActivity.findViewById(R.id.post_button).isShown();
        assertThat(postButtonIsShown, equalTo(true));
    }

    @Test
    public void onClick_checkInputStringsAreReadCorrectly() {
        final String expectedPostId = "7601";
        final String expectedNewContent = "Hello,+Ryane.";
        final String expectedUrl = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/7601";

        onView(withId(R.id.enter_post_id)).perform(ViewActions.typeText(expectedPostId), closeSoftKeyboard());
        onView(withId(R.id.enter_new_content)).perform(ViewActions.typeText("Hello, Ryane."), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(click());

        String actualPostId = blogPostUpdateActivity.getPostId();
        assertThat(actualPostId, equalTo(expectedPostId));

        String actualNewContent = blogPostUpdateActivity.getNewContent();
        assertThat(actualNewContent, equalTo(expectedNewContent));

        String actualUrl = blogPostUpdateActivity.getUrl();
        assertThat(actualUrl, equalTo(expectedUrl));

    }

    @Test
    public void onResponse_checkSuccessResponseAlertDialogIsShowing() {

        onView(withId(R.id.post_button)).perform(click());  // sets listener
        blogPostUpdateActivity.getRestRequest().getRestRequestListener().onResponse(new JSONObject());

        boolean isSuccessResponseShowing = blogPostUpdateActivity.getSuccessDialog().isShowing();
        assertThat(isSuccessResponseShowing, equalTo(true));

    }

    @Test
    public void onResponse_checkErrorResponseAlertDialogIsShowing() {
        // generate VolleyError to pass into AccessTokenRequestErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        onView(withId(R.id.post_button)).perform(click());  // sets errorlistener
        blogPostUpdateActivity.getRestRequest().getErrorListener().onErrorResponse(volleyError);

        boolean isErrorResponseShowing = blogPostUpdateActivity.getErrorDialog().isShowing();

        assertThat(isErrorResponseShowing, equalTo(true));

    }



}

