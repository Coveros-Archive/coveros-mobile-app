package com.coveros.coverosmobileapp.oauth;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Request that takes in and passes on an access to make an authenticated REST call.
 * @author Maria Kim
 */
public class RestRequest extends Request<JSONObject> {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_FORMAT = "Bearer %s";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public static final String ORIGINAL_RESPONSE_TAG = "Original response";
    public static final String APP_NAME = "CoverosMobileApp";

    private final Map<String, String> headers = new HashMap<>(2);
    private String body;

    private boolean isAuthenticated;

    private final RestRequestListener restRequestListener;
    private OnAuthFailedListener onAuthFailedListener;

    public enum RestMethod { GET, POST }
    private RestMethod restMethod;

    /**
     * @param url    url the request is made to
     * @param accessToken    access token for authentication that is passed with the request
     * @param body    content for POST request
     * @param restRequestListener    restRequestListener that responds on request success
     * @param restRequestErrorListener    restRequestListener that responds on request error
     */
    public RestRequest(String url, @Nullable String accessToken, @Nullable JSONObject body, RestRequestListener restRequestListener, RestRequestErrorListener restRequestErrorListener) {
        super(body == null ? Method.GET : Method.POST, url, restRequestErrorListener);
        if (body == null) {
            restMethod = RestMethod.GET;
        } else {
            this.body = body.toString();
            restMethod = RestMethod.POST;
        }
        this.restRequestListener = restRequestListener;
        if (accessToken == null) {
            isAuthenticated = false;
        } else {
            isAuthenticated = true;
            headers.put(AUTHORIZATION_HEADER, String.format(AUTHORIZATION_FORMAT, accessToken));
        }
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
        if (restRequestListener != null) {
            restRequestListener.onResponse(response);
        }
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
                Log.e(APP_NAME, "jsonString assigned to empty string", e);
            }

            JSONObject responseObject;
            try {
                responseObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                responseObject = new JSONObject();
                Log.e(APP_NAME, "responseObject assigned to empty JSONObject", e);

            }

            String restError = responseObject.optString("error", "");
            if ("authorization_required".equals(restError) || "invalid_token".equals(restError)) {
                onAuthFailedListener.onAuthFailed();
            }
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObject = new JSONObject();
            if (isJson(jsonString)) {
                jsonObject = jsonObjectFromResponse(jsonString);
            } else if (isJsonArray(jsonString)) {
                jsonObject = jsonArrayObjectFromResponse(jsonString);
            } else if (isJsonBoolean(jsonString)) {
                jsonObject = jsonBooleanObjectFromResponse(jsonString);
            }
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     * @return    body or null, because the super class depends on null being returned in certain cases, so you have to do it.
     */
    @Override
    @SuppressWarnings("squid:S1168")
    public byte[] getBody() {
        try {
            return body == null ? null : body.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    body, PROTOCOL_CHARSET);
            Log.e(APP_NAME, "getBody() will return null", uee);
            return null;
        }
    }

    /**
     * Tests if jsonString is a simple JSON string.
     * @param jsonString    string under test
     * "squid:S1848" suppressed because while JSONObject is not explicitly used, it is used to test if creating the object with jsonString throws the JSONException
     */
    @SuppressWarnings("squid:S1848")
    private boolean isJson(String jsonString) {
        try {
            new JSONObject(jsonString);
            return true;
        } catch (JSONException je) {
            Log.d(APP_NAME, "isJson is false", je);
            return false;
        }
    }

    /**
     * Tests if jsonString is a JSON array string.
     * @param jsonString    string under test
     */
    private boolean isJsonArray(String jsonString) {
        try {
            JSONArray responseArray = new JSONArray(jsonString);
            JSONObject wrapper = new JSONObject();
            wrapper.put(ORIGINAL_RESPONSE_TAG, responseArray);
            return true;
        } catch (JSONException je) {
            Log.d(APP_NAME, "isJsonArray is false", je);
            return false;
        }
    }

    /**
     * Tests if jsonString is a JSON boolean string.
     * @param jsonString    string under test
     */
    private boolean isJsonBoolean(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (jsonString.equals(Boolean.TRUE.toString())) {
                jsonObject.put(ORIGINAL_RESPONSE_TAG, true);
            }
            if (jsonString.equals(Boolean.FALSE.toString())) {
                jsonObject.put(ORIGINAL_RESPONSE_TAG, false);
            }
            return true;
        } catch (JSONException je) {
            Log.d(APP_NAME, "isJsonBoolean is false", je);
            return false;
        }
    }

    /**
     * Creates JSONObject from a string representation of a JSON.
     * @param jsonString    string to be used to create JSONObject
     * @return JSONObject    JSONObject created from jsonString
     * @throws JSONException    should never be thrown if used in conjunction with isJson[]() methods above
     */
    protected JSONObject jsonObjectFromResponse(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }

    /**
     * Creates JSONObject from a string representation of a JSON array.
     * @param jsonString    string to be used to create JSONObject
     * @return wrapper    JSONObject created from jsonString
     * @throws JSONException    should never be thrown if used in conjunction with isJson[]() methods above
     */
    protected JSONObject jsonArrayObjectFromResponse(String jsonString) throws JSONException {
        JSONArray responseArray = new JSONArray(jsonString);
        JSONObject wrapper = new JSONObject();
        wrapper.put(ORIGINAL_RESPONSE_TAG, responseArray);

        return wrapper;
    }

    /**
     * Creates JSONObject from a string representation of a JSON boolean.
     * @param jsonString    string to be used to create JSONObject
     * @return jsonObject    JSONObject created from jsonString
     * @throws JSONException    should never be thrown if used in conjunction with isJson[]() methods above
     */
    protected JSONObject jsonBooleanObjectFromResponse(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (jsonString.equals(Boolean.TRUE.toString())) {
            jsonObject.put(ORIGINAL_RESPONSE_TAG, true);
            return jsonObject;
        } else if (jsonString.equals(Boolean.FALSE.toString())) {
            jsonObject.put(ORIGINAL_RESPONSE_TAG, false);
            return jsonObject;
        } else {
            throw new JSONException("Not a valid JSON response: " + jsonString);
        }
    }

    public RestMethod getRestMethod() {
        return restMethod;
    }

    public RestRequestListener getRestRequestListener() {
        return restRequestListener;
    }

    public OnAuthFailedListener getOnAuthFailedListener() {
        return onAuthFailedListener;
    }

    public interface RestRequestListener extends Response.Listener<JSONObject> {
    }

    public interface RestRequestErrorListener extends Response.ErrorListener {
    }

    /**
     * AccessTokenRequestListener for an authorization failure.
     */
    public interface OnAuthFailedListener {
        void onAuthFailed();
    }


}