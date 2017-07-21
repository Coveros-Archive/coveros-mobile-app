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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Request class that can handle passing optional access token to make authenticated REST call.
 *
 * @author Maria Kim
 */
public class RestRequest extends Request<JsonObject> {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_FORMAT = "Bearer %s";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public static final String JSON_OBJECT_RESPONSE_TAG = "response";
    public static final String APP_NAME = "CoverosMobileApp";

    private final Map<String, String> headers = new HashMap<>(2);
    private String body;

    private boolean isAuthenticated;

    private final Response.Listener listener;
    private OnAuthFailedListener onAuthFailedListener;

    public enum RestMethod {GET, POST}

    public enum JsonType {OBJECT, ARRAY, PRIMITIVE}

    private RestMethod restMethod;

    /**
     * @param url                      url the request is made to
     * @param accessToken              access token for authentication that is passed with the request
     * @param body                     content for POST request
     * @param listener                 listener that responds on request success
     * @param restRequestErrorListener listener that responds on request error
     */
    public RestRequest(String url, @Nullable String accessToken, @Nullable JsonObject body, Response.Listener listener, Response.ErrorListener restRequestErrorListener) {
        super(body == null ? Method.GET : Method.POST, url, restRequestErrorListener);
        if (body == null) {
            restMethod = RestMethod.GET;
        } else {
            this.body = body.toString();
            restMethod = RestMethod.POST;
        }
        this.listener = listener;
        if (accessToken == null) {
            isAuthenticated = false;
        } else {
            isAuthenticated = true;
            headers.put(AUTHORIZATION_HEADER, String.format(AUTHORIZATION_FORMAT, accessToken));
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    protected void deliverResponse(JsonObject response) {
        if (listener != null) {
            listener.onResponse(response);
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

            JsonObject responseJson = (JsonObject) new JsonParser().parse(jsonString);

            String restError = responseJson.get("error").getAsString();
            if ("authorization_required".equals(restError) || "invalid_token".equals(restError)) {
                onAuthFailedListener.onAuthFailed();
            }
        }
    }

    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JsonObject responseJson = getTypedJsonObject(new JsonParser().parse(responseString));

            return Response.success(responseJson, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | UnsupportedJsonFormatException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     *
     * @return body or null, because the super class depends on null being returned in certain cases, so you have to do it.
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

    public JsonObject getTypedJsonObject(JsonElement jsonElement) throws UnsupportedJsonFormatException {
        JsonType jsonType = checkJsonType(jsonElement);
        JsonObject jsonObject = new JsonObject();
        if (jsonType == null) {
            throw new UnsupportedJsonFormatException("Json cannot be typed.");
        } else {
            switch (jsonType) {
                case OBJECT:
                    jsonObject = jsonElement.getAsJsonObject();
                    break;
                case ARRAY:
                    jsonObject.add(JSON_OBJECT_RESPONSE_TAG, jsonElement.getAsJsonArray());
                    break;
                case PRIMITIVE:
                    jsonObject.add(JSON_OBJECT_RESPONSE_TAG, jsonElement.getAsJsonPrimitive());
                    break;
            }
            return jsonObject;
        }
    }

    public JsonType checkJsonType(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return JsonType.OBJECT;
        } else if (jsonElement.isJsonArray()) {
            return JsonType.ARRAY;
        } else if (jsonElement.isJsonPrimitive()) {
            return JsonType.PRIMITIVE;
        } else {
            return null;
        }
    }

    /**
     * Sets the OnAuthFailedListener for the request.
     *
     * @param onAuthFailedListener
     */
    public void setOnAuthFailedListener(OnAuthFailedListener onAuthFailedListener) {
        if (isAuthenticated) {
            this.onAuthFailedListener = onAuthFailedListener;
        } else {
            Log.e("RestRequest", "Not an authenticated request: onAuthFailedListener was not set");
        }
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public RestMethod getRestMethod() {
        return restMethod;
    }

    public Response.Listener getListener() {
        return listener;
    }

    public OnAuthFailedListener getOnAuthFailedListener() {
        return onAuthFailedListener;
    }

    /**
     * AccessTokenRequestListener for an authorization failure.
     */
    public interface OnAuthFailedListener {
        void onAuthFailed();
    }

    /**
     * Thrown when Json String cannot be typed as JsonObject, JsonArray, or JsonPrimitive
     */
    class UnsupportedJsonFormatException extends Exception {
        public UnsupportedJsonFormatException(String message) {
            super(message);
        }
    }


}