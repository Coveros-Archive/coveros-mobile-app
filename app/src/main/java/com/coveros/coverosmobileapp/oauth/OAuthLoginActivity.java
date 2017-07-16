package com.coveros.coverosmobileapp.oauth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.website.CustomWebViewClient;

/**
 * Displays a WebView that presents user with log-in page.
 * @author Maria Kim
 */

public class OAuthLoginActivity extends AppCompatActivity {

    private final String AUTH_ENDPOINT = "https://www3.dev.secureci.com/oauth/authorize";
    private final String TOKEN_ENDPOINT = "https://www3.dev.secureci.com/oauth/token";
    private final String CLIENT_ID = "ZIgsZCSNWZ0R869u10Y7ZNFSpn4y2S";
    private final String CLIENT_SECRET = "w1ANtApmollpctwlYxmXFlKke9HiIK";
    private final String REDIRECT_URI = "com.coveros.coverosmobileapp://oauthresponse";
    private final String GRANT_TYPE = "authorization_code";

    private AccessTokenRequest accessTokenRequest;
    private AlertDialog errorResponse;

    private AuthCallback authCallback = new AuthCallback() {
        @Override
        public void onSuccess(String authCode) {
            accessTokenRequest = new AccessTokenRequest(TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, authCode, GRANT_TYPE, new AccessTokenRequest.Listener() {
                @Override
                public void onResponse(String response) {
                    clearCookies();
                    Intent intent = new Intent(getApplicationContext(), BlogPostUpdateActivity.class);
                    intent.putExtra("accessToken", response);
                    startActivity(intent);
                    finish();
                }
            }, new AccessTokenRequest.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorResponse = createRequestResponse(OAuthLoginActivity.this);
                    errorResponse.show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(OAuthLoginActivity.this);
            requestQueue.add(accessTokenRequest);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth);

        String responseType = "code";
        AuthUrl authUrl = new AuthUrl(AUTH_ENDPOINT, CLIENT_ID, REDIRECT_URI, responseType);

        WebView login = (WebView) findViewById(R.id.login);
        Log.d("Auth url", authUrl.toString());
        login.loadUrl(authUrl.toString());

        setWebViewClient(login, authCallback);
    }

    /**
     * Sets the WebViewClient that watches the WebView for the redirect URI that contains the authorization code.
     * @param webView    WebView for which the WebViewClient is set
     * @param authCallback    callback to wait for the parsing of the authorization code from the redirect uri
     */
    private void setWebViewClient(WebView webView, final AuthCallback authCallback) {
        webView.setWebViewClient(new CustomWebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(REDIRECT_URI)) {
                    Uri uri = Uri.parse(url);
                    authCallback.onSuccess(uri.getQueryParameter("code"));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * Clears cookies so that user has to log-in each time to perform the authorization.
     */
    private void clearCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
            }
        });
    }

    /**
     * Creates an AlertDialog to display the response (success or error) from the REST request.
     * @param context    context on which to display the AlertDialog
     * @return    AlertDialog with these data
     */
    AlertDialog createRequestResponse(Context context) {
        final String errorTitle = context.getString(R.string.post_update_request_response_error_title);
        final String errorMessage = context.getString(R.string.post_update_request_response_error_message);
        final String buttonText = context.getString(R.string.post_update_request_response_dismiss_button);
        AlertDialog requestResponse = new AlertDialog.Builder(context).create();
        requestResponse.setTitle(errorTitle);
        requestResponse.setMessage(errorMessage);
        requestResponse.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return requestResponse;
    }

    AccessTokenRequest getAccessTokenRequest() {
        return accessTokenRequest;
    }

    AlertDialog getErrorResponse() {
        return errorResponse;
    }

    AuthCallback getAuthCallback() {
        return authCallback;
    }

    interface AuthCallback {
        void onSuccess(String authCode);
    }

}
