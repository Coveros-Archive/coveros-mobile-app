package com.coveros.coverosmobileapp.oauth.example;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;
import com.coveros.coverosmobileapp.errorlistener.NetworkErrorListener;
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
    private AlertDialog accessTokenRequestErrorAlertDialog;
    private NetworkErrorListener networkErrorListener;

    WebViewAuthCallback webViewAuthCallback = new WebViewAuthCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth);

        final String accessTokenRequestErrorAlertDialogMessage = getString(R.string.oauth_login_access_token_error_message);
        accessTokenRequestErrorAlertDialog = AlertDialogFactory.createErrorAlertDialogDefaultButton(OAuthLoginActivity.this, accessTokenRequestErrorAlertDialogMessage);
        networkErrorListener = new NetworkErrorListener(OAuthLoginActivity.this, accessTokenRequestErrorAlertDialog);

        String responseType = "code";
        AuthUrl authUrl = new AuthUrl(AUTH_ENDPOINT, CLIENT_ID, REDIRECT_URI, responseType);

        WebView login = (WebView) findViewById(R.id.login);
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

    AccessTokenRequest getAccessTokenRequest() {
        return accessTokenRequest;
    }

    AlertDialog getAccessTokenRequestErrorAlertDialog() {
        return accessTokenRequestErrorAlertDialog;
    }

    AuthCallback getWebViewAuthCallback() {
        return webViewAuthCallback;
    }

    class WebViewAuthCallback implements AuthCallback {
        @Override
        public void onSuccess(String authCode) {
            accessTokenRequest = new AccessTokenRequest(TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, authCode, GRANT_TYPE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                clearCookies();
                Intent intent = new Intent(getApplicationContext(), BlogPostUpdateActivity.class);
                intent.putExtra("accessToken", response);
                startActivity(intent);
                finish();
                }
            }, networkErrorListener);

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
