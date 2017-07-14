package com.coveros.coverosmobileapp.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maria Kim
 */

public class AccessTokenRequest extends com.android.volley.toolbox.StringRequest {

    public interface Listener extends Response.Listener<String> {
    }

    public interface ErrorListener extends Response.ErrorListener {
    }

    private Map<String, String> params = new HashMap<String, String>();

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authCode;
    private String grantType;

    private final String CLIENT_ID_KEY = "client_id";
    private final String CLIENT_SECRET_KEY = "client_secret";
    private final String REDIRECT_URI_KEY = "redirect_urI";
    private final String CODE_KEY = "code";
    private final String GRANT_TYPE_KEY = "grant_type";

    private Listener listener;

    AccessTokenRequest(String endpoint, String clientId, String clientSecret, String redirectUri, String authCode, String grantType, Listener listener, ErrorListener errorListener) {
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
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject tokenData = new JSONObject(jsonString);
            String token = tokenData.getString("access_token");
            return Response.success(token, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
