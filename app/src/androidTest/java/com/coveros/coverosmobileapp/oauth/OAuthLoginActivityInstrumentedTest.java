package com.coveros.coverosmobileapp.oauth;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.webkit.WebView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Maria Kim
 */

public class OAuthLoginActivityInstrumentedTest extends LooperTestSuite {

    OAuthLoginActivity oAuthLoginActivity;

    @Rule
    public ActivityTestRule<OAuthLoginActivity> oAuthLoginActivityRule = new ActivityTestRule<OAuthLoginActivity>(OAuthLoginActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            return intent;
        }
    };

    @Before
    public void setUp() {
            oAuthLoginActivity = oAuthLoginActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkLogInWebViewIsShown() {
        WebView login = (WebView) oAuthLoginActivity.findViewById(R.id.login);

        boolean isLoginWebViewShowing = login.isShown();
        assertThat(isLoginWebViewShowing, equalTo(true));
    }

    @Test
    public void onErrorResponse_checkErrorResponseAlertDialogIsShowing() {
        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        oAuthLoginActivity.getAuthCallback().onSuccess("525");
        oAuthLoginActivity.getAccessTokenRequest().getErrorListener().onErrorResponse(volleyError);

        boolean isErrorResponseShowing = oAuthLoginActivity.getErrorResponse().isShowing();

        assertThat(isErrorResponseShowing, equalTo(isErrorResponseShowing));
    }
}
