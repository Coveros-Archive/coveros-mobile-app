package com.coveros.coverosmobileapp.oauth.example;

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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.oauth.AccessTokenRequest;
import com.coveros.coverosmobileapp.oauth.AuthUrl;

/**
 * Displays a WebView that presents user with log-in page.
 * @author Maria Kim
 */

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class OAuthLoginActivity extends AppCompatActivity {

    private static final String AUTH_ENDPOINT = "https://www3.dev.secureci.com/oauth/authorize";
    private static final String TOKEN_ENDPOINT = "https://www3.dev.secureci.com/oauth/token";
    private static final String CLIENT_ID = "ZIgsZCSNWZ0R869u10Y7ZNFSpn4y2S";
    private static final String CLIENT_SECRET = "w1ANtApmollpctwlYxmXFlKke9HiIK";
    private static final String REDIRECT_URI = "com.coveros.coverosmobileapp://oauthresponse";
    private static final String GRANT_TYPE = "authorization_code";

    private AccessTokenRequest accessTokenRequest;
    private AlertDialog errorDialog;

    WebViewAuthCallback webViewAuthCallback = new WebViewAuthCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth);

        String responseType = "code";
        AuthUrl authUrl = new AuthUrl(AUTH_ENDPOINT, CLIENT_ID, REDIRECT_URI, responseType);

        WebView login = (WebView) findViewById(R.id.login);
        Log.d("Auth url", authUrl.toString());
        login.loadUrl(authUrl.toString());

        setWebViewClient(login, webViewAuthCallback);
    }

    /**
     * Sets the WebViewClient that watches the WebView for the redirect URI that contains the authorization code.
     * @param webView    WebView for which the WebViewClient is set
     * @param authCallback    callback to wait for the parsing of the authorization code from the redirect uri
     */
    private void setWebViewClient(WebView webView, final AuthCallback authCallback) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
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
     * Creates an AlertDialog to display the response (success or error) from the REST request.
     * @param context    context on which to display the AlertDialog
     * @return    AlertDialog with these data
     */
    AlertDialog createAccessTokenRequestErrorDialog(Context context) {
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

    AlertDialog getErrorDialog() {
        return errorDialog;
    }

    AuthCallback getWebViewAuthCallback() {
        return webViewAuthCallback;
    }

    class WebViewAuthCallback implements AuthCallback {
        @Override
        public void onSuccess(String authCode) {
            accessTokenRequest = new AccessTokenRequest(TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, authCode, GRANT_TYPE, new AccessTokenRequest.AccessTokenRequestListener() {
                @Override
                public void onResponse(String response) {
                    clearCookies();
                    Intent intent = new Intent(getApplicationContext(), BlogPostUpdateActivity.class);
                    intent.putExtra("accessToken", response);
                    startActivity(intent);
                    finish();
                }
            }, new AccessTokenRequest.AccessTokenRequestErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorDialog = createAccessTokenRequestErrorDialog(OAuthLoginActivity.this);
                    if (!isFinishing()) {
                        errorDialog.show();
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(OAuthLoginActivity.this);
            requestQueue.add(accessTokenRequest);
        }
        /**
         * Clears cookies so that user has to log-in each time to perform the authorization.
         */
        private void clearCookies() {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    // don't need to do anything with the value
                }
            });
        }

    }

    interface AuthCallback {
        void onSuccess(String authCode);
    }

}
