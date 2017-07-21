package com.coveros.coverosmobileapp.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Maria Kim
 */

public class RestRequestInstrumentedTest {

    private RestRequest postRequest;
    private RestRequest getRequest;
    private RestRequest unauthenticatedRequest;

    private boolean postRequestOnAuthFailedCalled;
    private boolean getRequestOnAuthFailedCalled;

    @Before
    public void setUp() throws JSONException {

        postRequestOnAuthFailedCalled = false;
        getRequestOnAuthFailedCalled = false;

        // setting up post request
        JsonObject body = new JsonObject();
        body.addProperty("content", "I Love cats, with a capital L.");
        postRequest = new RestRequest("https://rtykl525.com", "525", body, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        postRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
            @Override
            public void onAuthFailed() {
                postRequestOnAuthFailedCalled = true;
            }
        });

        // setting up get request
        getRequest = new RestRequest("https://rtykl525.com", "525", null, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        getRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
            @Override
            public void onAuthFailed() {
                getRequestOnAuthFailedCalled = true;
            }
        });

        unauthenticatedRequest = new RestRequest("https://rtykl525.com", null, null, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        unauthenticatedRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
            @Override
            public void onAuthFailed() {
                // does nothing because onAuthFailureListener should not be set because this is an unauthenticated request
            }
        });

    }

    @Test
    public void getRestMethod_withBody_checkPostMethod() {

        RestRequest.RestMethod actualRestMethod = postRequest.getRestMethod();
        assertThat(actualRestMethod, equalTo(RestRequest.RestMethod.POST));

    }

    @Test
    public void getRestMethod_withoutBody_checkGetMethod() {

        RestRequest.RestMethod actualRestMethod = getRequest.getRestMethod();
        assertThat(actualRestMethod, equalTo(RestRequest.RestMethod.GET));

    }

    @Test
    public void setOnAuthFailedListener_checkOnAuthFailedListenerNotNull() {

        assertThat(postRequest.getIsAuthenticated(), equalTo(true));
        assertThat(getRequest.getIsAuthenticated(), equalTo(true));
        assertThat(postRequest.getOnAuthFailedListener(), is(notNullValue()));
        assertThat(getRequest.getOnAuthFailedListener(), is(notNullValue()));

    }

    @Test
    public void setOnAuthFailedListener_withUnauthenticatedRequest_checkOnAuthFailedListenerIsNull() {
        assertThat(unauthenticatedRequest.getIsAuthenticated(), equalTo(false));
        assertThat(unauthenticatedRequest.getOnAuthFailedListener(), is(nullValue()));
    }


    @Test
    public void getHeaders_checkAccessTokenIsCorrect() {

        String actualPostRequestAccessToken = postRequest.getHeaders().get("Authorization");
        assertThat(actualPostRequestAccessToken, equalTo("Bearer 525"));

        String actualGetRequestAccessToken = getRequest.getHeaders().get("Authorization");
        assertThat(actualGetRequestAccessToken, equalTo("Bearer 525"));

    }

    @Test
    public void deliverError_withAuthRequiredError() {
        // generate VolleyError with authorization_required error
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("error", "authorization_required");
        byte[] dataBytes = dataJson.toString().getBytes();
        NetworkResponse networkResponse = new NetworkResponse(400, dataBytes, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        postRequest.deliverError(volleyError);
        assertThat(postRequestOnAuthFailedCalled, equalTo(true));

        getRequest.deliverError(volleyError);
        assertThat(getRequestOnAuthFailedCalled, equalTo(true));
    }

    @Test
    public void deliverError_withInvalidTokenError() {
        // generate VolleyError with authorization_required error
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("error", "invalid_token");
        byte[] dataBytes = dataJson.toString().getBytes();
        NetworkResponse networkResponse = new NetworkResponse(400, dataBytes, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        postRequest.deliverError(volleyError);
        assertThat(postRequestOnAuthFailedCalled, equalTo(true));

        getRequest.deliverError(volleyError);
        assertThat(getRequestOnAuthFailedCalled, equalTo(true));
    }

    @Test
    public void parseNetworkResponse_withValidNetworkResponse() {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("format", "standard");
        byte[] dataBytes = dataJson.toString().getBytes();
        Map<String, String> headers = new HashMap<>();

        Response<JsonObject> postRequestResponse = postRequest.parseNetworkResponse(new NetworkResponse(dataBytes, headers));
        String actualPostFormat = postRequestResponse.result.get("format").getAsString();

        assertThat(actualPostFormat, equalTo("standard"));

        Response<JsonObject> getRequestResponse = postRequest.parseNetworkResponse(new NetworkResponse(dataBytes, headers));
        String actualGetFormat = getRequestResponse.result.get("format").getAsString();

        assertThat(actualGetFormat, equalTo("standard"));

    }

    @Test
    public void getBody() {
        byte[] actualPostBody = postRequest.getBody();
        String actualPostContent = ((JsonObject) new JsonParser().parse(new String(actualPostBody))).get("content").getAsString();
        assertThat(actualPostContent, equalTo("I Love cats, with a capital L."));

        byte[] actualGetBody = getRequest.getBody();
        assertThat(actualGetBody, is(nullValue()));
    }

    @Test
    public void checkJsonType_withJsonObject() {
        RestRequest.JsonType expectedJsonType = RestRequest.JsonType.OBJECT;
        JsonElement jsonElement = new JsonParser().parse("{\"content\": \"I Love cats, with a capital L.\"}");
        RestRequest.JsonType actualJsonType = postRequest.checkJsonType(jsonElement);

        assertThat(actualJsonType, equalTo(expectedJsonType));
    }

    @Test
    public void checkJsonType_withJsonArray() {
        RestRequest.JsonType expectedJsonType = RestRequest.JsonType.ARRAY;
        JsonElement jsonElement = new JsonParser().parse("[{\"content\":\"I Love cats, with a capital L.\"},{\"content\":\"I Love cats, with a capital L.\"}]");
        RestRequest.JsonType actualJsonType = postRequest.checkJsonType(jsonElement);

        assertThat(actualJsonType, equalTo(expectedJsonType));
    }

    @Test
    public void checkJsonType_withJsonPrimitive() {
        RestRequest.JsonType expectedJsonType = RestRequest.JsonType.PRIMITIVE;
        JsonElement jsonElement = new JsonParser().parse("true");
        RestRequest.JsonType actualJsonType = postRequest.checkJsonType(jsonElement);

        assertThat(actualJsonType, equalTo(expectedJsonType));
    }

    @Test
    public void getTypedJsonObject_withJsonObject() throws RestRequest.UnsupportedJsonFormatException {
        JsonElement jsonElement = new JsonParser().parse("{\"content\": \"I Love cats, with a capital L.\"}");
        JsonObject jsonObject = postRequest.getTypedJsonObject(jsonElement);

        String expectedFirstValue = "I Love cats, with a capital L.";
        String actualFirstValue = jsonObject.get("content").getAsString();

        assertThat(actualFirstValue, equalTo(expectedFirstValue));
    }

    @Test
    public void getTypedJsonObject_withJsonArray() throws RestRequest.UnsupportedJsonFormatException {
        JsonElement jsonElement = new JsonParser().parse("[{\"content\":\"I Love cats, with a capital L.\"},{\"content\":\"I Love cats, with a capital L.\"}]");
        JsonObject jsonObject = postRequest.getTypedJsonObject(jsonElement);

        JsonArray expectedFirstValue = (JsonArray) new JsonParser().parse("[{\"content\":\"I Love cats, with a capital L.\"},{\"content\":\"I Love cats, with a capital L.\"}]");
        JsonArray actualFirstValue = jsonObject.get("response").getAsJsonArray();

        assertThat(actualFirstValue, equalTo(expectedFirstValue));

    }

    @Test
    public void getTypedJsonObject_withJsonPrimitive() throws RestRequest.UnsupportedJsonFormatException {
        JsonElement jsonElement = new JsonParser().parse("true");
        JsonObject jsonObject = postRequest.getTypedJsonObject(jsonElement);

        JsonPrimitive expectedFirstValue = (JsonPrimitive) (new JsonParser()).parse("true");
        JsonPrimitive actualFirstValue = jsonObject.get("response").getAsJsonPrimitive();

        assertThat(actualFirstValue, equalTo(expectedFirstValue));


    }
}
