package com.coveros.coverosmobileapp.oauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.blogpost.BlogPostReadActivity;
import com.coveros.coverosmobileapp.website.CustomWebViewClient;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maria Kim
 */

public class OAuthLogin extends AppCompatActivity {

    private final String AUTH_ENDPOINT = "https://www3.dev.secureci.com/oauth/authorize";
    private final String TOKEN_ENDPOINT = "https://www3.dev.secureci.com/oauth/token";
    private final String CLIENT_ID = "ZIgsZCSNWZ0R869u10Y7ZNFSpn4y2S";
    private final String CLIENT_SECRET = "w1ANtApmollpctwlYxmXFlKke9HiIK";
    private final String REDIRECT_URI = "com.coveros.coverosmobileapp://oauthresponse";
    private final String GRANT_TYPE = "authorization_code";

    private WebView login;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth);

        String responseType = "code";
        String scope = "global";
        AuthUrl authUrl = new AuthUrl(AUTH_ENDPOINT, CLIENT_ID, REDIRECT_URI, responseType, scope);

        WebView login = (WebView) findViewById(R.id.login);
        Log.d("Auth url", authUrl.toString());
        login.loadUrl(authUrl.toString());

        setWebViewClient(login, new AuthCallback() {
            @Override
            public void onSuccess(String authCode) {
                AccessTokenRequest accessTokenRequest = new AccessTokenRequest(TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, authCode, GRANT_TYPE, new AccessTokenRequest.Listener() {
                    @Override
                    public void onResponse(String response) {
                        clearCookies();
                        Intent intent = new Intent(getApplicationContext(), BlogPostUpdater.class);
                        intent.putExtra("accessToken", response);
                        startActivity(intent);
                        finish();
                    }
                }, new AccessTokenRequest.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley error", error.toString());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(OAuthLogin.this);
                requestQueue.add(accessTokenRequest);
            }
        });
    }

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

    public void clearCookies(){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
            }
        });
    }

    interface AuthCallback {
        void onSuccess(String authCode);
    }

}
