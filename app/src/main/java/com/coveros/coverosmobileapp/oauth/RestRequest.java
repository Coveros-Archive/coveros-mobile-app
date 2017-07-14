package com.coveros.coverosmobileapp.oauth;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.Map;
import java.util.HashMap;

import java.io.UnsupportedEncodingException;

/**
 * Request that takes in and passes on an access to make an authenticated REST call.
 * @author Maria Kim
 */
public class RestRequest extends Request<JSONObject> {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_FORMAT = "Bearer %s";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);


    public static final String ORIGINAL_RESPONSE = "originalResponse";  // NOT SURE IF I NEED THIS

    public interface Listener extends Response.Listener<JSONObject> {
    }

    public interface ErrorListener extends Response.ErrorListener {
    }

    /**
     * Listener for an authorization failure.
     */
    public interface OnAuthFailedListener {
        void onAuthFailed();
    }

    private final Map<String, String> headers = new HashMap<String, String>(2);

    private final Listener listener;
    private static OnAuthFailedListener onAuthFailedListener;

    private final Map<String, String> params = null;

    private String body;

    /**
     * @param url    url the request is made to
     * @param accessToken    access token for authentication that is passed with the request
     * @param body    content for POST request
     * @param listener    listener that responds on request success
     * @param errorListener    listener that responds on request error
     */
    public RestRequest(String url, String accessToken, @Nullable JSONObject body, Listener listener, ErrorListener errorListener) {
        super(body == null ? Method.GET : Method.POST, url, errorListener);
        if (body != null) {
            this.body = body.toString();
        }
        this.listener = listener;
        headers.put(AUTHORIZATION_HEADER, String.format(AUTHORIZATION_FORMAT, accessToken));
    }

    /**
     * Sets the OnAuthFailedListener for the request.
     * @param onAuthFailedListener
     */
    public void setOnAuthFailedListener(OnAuthFailedListener onAuthFailedListener) {
        this.onAuthFailedListener = onAuthFailedListener;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

        // fire OnAuthFailedListener if we receive an invalid token error
        if (error.networkResponse != null && error.networkResponse.statusCode >= 400 && onAuthFailedListener != null) {
            String jsonString;
            try {
                jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                jsonString = "";
            }

            JSONObject responseObject;
            try {
                responseObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                responseObject = new JSONObject();
            }

            String restError = responseObject.optString("error", "");
            if (restError.equals("authorization_required") || restError.equals("invalid_token")) {
                onAuthFailedListener.onAuthFailed();
            }
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            JSONObject jsonObject;
            try {
                jsonObject = jsonObjectFromResponse(jsonString);
            } catch (JSONException jsonException) {
                try {
                    jsonObject = jsonArrayObjectFromResponse(jsonString);
                } catch (JSONException jsonArrayException) {
                    try {
                        jsonObject = jsonBooleanObjectFromResponse(jsonString);
                    } catch (JSONException jsonBooleanException) {
                        return Response.error(new ParseError(jsonBooleanException));
                    }
                }
            }

            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    protected JSONObject jsonObjectFromResponse(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }

    protected JSONObject jsonArrayObjectFromResponse(String jsonString) throws JSONException {
        JSONArray responseArray = new JSONArray(jsonString);
        JSONObject wrapper = new JSONObject();
        wrapper.put(ORIGINAL_RESPONSE, responseArray);

        return wrapper;
    }

    protected JSONObject jsonBooleanObjectFromResponse(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (jsonString.equals(Boolean.TRUE.toString())) {
            jsonObject.put(ORIGINAL_RESPONSE, true);
            return jsonObject;
        } else if (jsonString.equals(Boolean.FALSE.toString())) {
            jsonObject.put(ORIGINAL_RESPONSE, false);
            return jsonObject;
        } else {
            throw new JSONException("Not a valid JSON response: " + jsonString);
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return body == null ? null : body.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    body, PROTOCOL_CHARSET);
            return null;
        }
    }

}