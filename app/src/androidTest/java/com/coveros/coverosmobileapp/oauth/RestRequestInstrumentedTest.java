package com.coveros.coverosmobileapp.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
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

    RestRequest postRequest;
    RestRequest getRequest;

    boolean postRequestOnAuthFailedCalled;
    boolean getRequestOnAuthFailedCalled;

    @Before
    public void setUp() throws JSONException {

        postRequestOnAuthFailedCalled = false;
        getRequestOnAuthFailedCalled = false;

        // setting up post request
        JSONObject body = new JSONObject();
        body.put("content", "I Love cats, with a capital L.");
        postRequest = new RestRequest("https://rtykl525.com", "525", body, new RestRequest.Listener() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new RestRequest.ErrorListener() {
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
        getRequest = new RestRequest("https://rtykl525.com", "525", null, new RestRequest.Listener() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new RestRequest.ErrorListener() {
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
    public void setOnAuthFailedListener_checkPostRequestOnAuthFailedListenerNotNull() {

        assertThat(postRequest.getOnAuthFailedListener(), is(notNullValue()));
    }

    @Test
    public void setOnAuthFailedListener_checkGetRequestOnAuthFailedListenerNotNull() {

        assertThat(getRequest.getOnAuthFailedListener(), is(notNullValue()));
    }

    @Test
    public void getHeaders_checkAccessTokenIsCorrect() {

        String actualPostRequestAccessToken = postRequest.getHeaders().get("Authorization");
        assertThat(actualPostRequestAccessToken, equalTo("Bearer 525"));

        String actualGetRequestAccessToken = getRequest.getHeaders().get("Authorization");
        assertThat(actualGetRequestAccessToken, equalTo("Bearer 525"));

    }

    @Test
    public void deliverError_withAuthRequiredError() throws JSONException {
        // generate VolleyError with authorization_required error
        JSONObject dataJson = new JSONObject();
        dataJson.put("error", "authorization_required");
        byte[] dataBytes = dataJson.toString().getBytes();
        NetworkResponse networkResponse = new NetworkResponse(400, dataBytes, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        postRequest.deliverError(volleyError);
        assertThat(postRequestOnAuthFailedCalled, equalTo(true));

        getRequest.deliverError(volleyError);
        assertThat(getRequestOnAuthFailedCalled, equalTo(true));
    }

    @Test
    public void deliverError_withInvalidTokenError() throws JSONException {
        // generate VolleyError with authorization_required error
        JSONObject dataJson = new JSONObject();
        dataJson.put("error", "invalid_token");
        byte[] dataBytes = dataJson.toString().getBytes();
        NetworkResponse networkResponse = new NetworkResponse(400, dataBytes, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        postRequest.deliverError(volleyError);
        assertThat(postRequestOnAuthFailedCalled, equalTo(true));

        getRequest.deliverError(volleyError);
        assertThat(getRequestOnAuthFailedCalled, equalTo(true));
    }

    @Test
    public void parseNetworkResponse_withValidNetworkResponse() throws JSONException {
        JSONObject dataJson = new JSONObject();
        dataJson.put("format", "standard");
        byte[] dataBytes = dataJson.toString().getBytes();
        Map<String, String> headers = new HashMap<>();

        Response<JSONObject> postRequestResponse = postRequest.parseNetworkResponse(new NetworkResponse(dataBytes, headers));
        String actualPostFormat = postRequestResponse.result.getString("format");

        assertThat(actualPostFormat, equalTo("standard"));

        Response<JSONObject> getRequestResponse = postRequest.parseNetworkResponse(new NetworkResponse(dataBytes, headers));
        String actualGetFormat = getRequestResponse.result.getString("format");

        assertThat(actualGetFormat, equalTo("standardR"));

    }

    @Test
    public void getBody() throws JSONException {
        byte[] actualPostBody = postRequest.getBody();
        String actualPostContent = new JSONObject(new String(actualPostBody)).getString("content");
        assertThat(actualPostContent, equalTo("I Love cats, with a capital L."));

        byte[] actualGetBody = getRequest.getBody();
        assertThat(actualGetBody, is(nullValue()));
    }



}
