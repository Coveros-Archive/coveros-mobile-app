package com.coveros.coverosmobileapp.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A request for an access token in the form of a String.
 * @author Maria Kim
 */

public class AccessTokenRequest extends com.android.volley.toolbox.StringRequest {

    private Map<String, String> params = new HashMap<>();

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authCode;
    private String grantType;

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String REDIRECT_URI_KEY = "redirect_uri";
    private static final String CODE_KEY = "code";
    private static final String GRANT_TYPE_KEY = "grant_type";

    private Response.Listener listener;

    /**
     * @param endpoint    url the POST request is made to
     * @param clientId    client id provided by WP OAuth Server
     * @param clientSecret    client secret provided by WP OAuth Server
     * @param redirectUri    redirect uri that was used to gain authorization code in the WebView in OAuthLoginActivity
     * @param authCode    authorization code received after user logs in
     * @param grantType    grant type ("authorization_code")
     * @param listener    listener that responds on request success
     * @param errorListener    error listener that responds on request error
     * "squid:S00107" warning, which is about the constructor taking 8 parameters rather than the 7 allowed. Ryan said this code smell is appropriate in this context and not a priority to address.
     */
    @SuppressWarnings("squid:S00107")
    public AccessTokenRequest(String endpoint, String clientId, String clientSecret, String redirectUri, String authCode, String grantType, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, endpoint, listener, errorListener);

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.authCode = authCode;
        this.grantType = grantType;
        this.listener = listener;

        params.put(CLIENT_ID_KEY, this.clientId);
        params.put(CLIENT_SECRET_KEY, this.clientSecret);
        params.put(REDIRECT_URI_KEY, this.redirectUri);
        params.put(CODE_KEY, this.authCode);
        params.put(GRANT_TYPE_KEY, this.grantType);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public void deliverResponse(String token) {
        listener.onResponse(token);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JsonObject responseJson  = (JsonObject) new JsonParser().parse(responseString);
            String token = responseJson.get("access_token").getAsString();
            return Response.success(token, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
