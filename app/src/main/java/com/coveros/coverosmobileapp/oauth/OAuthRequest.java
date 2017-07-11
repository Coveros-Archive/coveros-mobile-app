package com.coveros.coverosmobileapp.oauth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.List;

/**
 * @author Maria Kim
 */

public class OAuthRequest extends AppCompatActivity {

    OAuth oAuth;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        oAuth = new OAuth("ZIgsZCSNWZ0R869u10Y7ZNFSpn4y2S", "w1ANtApmollpctwlYxmXFlKke9HiIK", "com.coveros.coverosmobileapp://oauthresponse");
        Log.d("IS VIEW AVAILABLE: ", "" + isAvailable(OAuthRequest.this, new Intent(Intent.ACTION_VIEW,
                Uri.parse(oAuth.getAppRedirectURI() + "&response_type=code") )));


    }

    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Uri uri = intent.getData();

        if (uri != null && uri.toString()
                .startsWith("com.coveros.coverosmobileapp://oauthresponse"))
        {
            String code = uri.getQueryParameter("code");
            OAuth.BearerRequest bearerRequest = (OAuth.BearerRequest) oAuth.makeRequest(code, new OAuth.Listener() {
                @Override
                public void onResponse(OAuth.Token response) {
                    Log.d("Token: ", response.toString());
                }
            }, new OAuth.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error: ", error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(OAuthRequest.this);
            requestQueue.add(bearerRequest);
        }
    }
}
