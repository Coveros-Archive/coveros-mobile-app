package com.coveros.coverosmobileapp.oauth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.website.CustomWebViewClient;


import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coveros.coverosmobileapp.oauth.RestClient.REST_API_ENDPOINT_URL_V2;

/**
 * @author Maria Kim
 */

public class OAuthRequest extends AppCompatActivity {

    private String authCode;
    private WebView authorization;
    private OAuth oAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth);
        oAuth = new OAuth("ZIgsZCSNWZ0R869u10Y7ZNFSpn4y2S", "w1ANtApmollpctwlYxmXFlKke9HiIK", "com.coveros.coverosmobileapp://oauthresponse");

        authorization = (WebView) findViewById(R.id.authorization);
        Log.d("OAUTH", oAuth.getAuthorizationURL());
        authorization.loadUrl(oAuth.getAuthorizationURL());
        setAuthorizationWebViewClient(new OAuthCallback() {
            @Override
            public void onSuccess(boolean isRedirected) {
                Log.d("isRedirected", "" + isRedirected);
                Log.d("Auth Code", authCode);
                OAuth.BearerRequest bearerRequest = (OAuth.BearerRequest) oAuth.makeRequest(authCode, new OAuth.Listener() {
                    @Override
                    public void onResponse(OAuth.Token response) {
                        Log.d("Token", response.toString());
                        RequestQueue wordpressRequestQueue = Volley.newRequestQueue(OAuthRequest.this);
                        RestClient restClient = new RestClient(wordpressRequestQueue, response.toString(), "https://www3.dev.secureci.com/wp-json/wp/v2/");
                        Map<String, String> body = new HashMap<>();
                        body.put("content", "even better content");
                        RestRequest restRequest = restClient.post("https://www3.dev.secureci.com/wp-json/wp/v2/posts/7509", body, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("SUCCESSSSSS", "SUCCESSFUL POST");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("FAILURE", "FAILLLLLLLL");
                            }
                        });
                        wordpressRequestQueue.add(restRequest);

                    }
                }, new OAuth.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(OAuthRequest.this);
                requestQueue.add(bearerRequest);
            }
        });
        clearCookies();
    }

    private void setAuthorizationWebViewClient(final OAuthCallback oAuthCallback) {
        authorization.setWebViewClient(new CustomWebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(oAuth.getAppRedirectURI())) {
                    Uri uri = Uri.parse(url);
                    Log.d("URI", uri.toString());
                    setAuthCode(uri.getQueryParameter("code"));
                    oAuthCallback.onSuccess(true);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void clearCookies(){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                Log.d("Removing cookes", value + "");
            }
        });
    }

    interface OAuthCallback {
        void onSuccess(boolean isRedirected);
    }

}
