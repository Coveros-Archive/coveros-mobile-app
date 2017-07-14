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
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Running this as an instrumented test because running as a unit test gives a UnsatisfiedLinkError
 * @author Maria Kim
 */

public class AccessTokenRequestInstrumentedTest {

    AccessTokenRequest accessTokenRequest;
    boolean isListenerOnResponseCalled = false;

    @Before
    public void setUp() {
        accessTokenRequest = new AccessTokenRequest("https://rtykl525.com", "12345", "54321", "https://rtykl525.com/redirect", "525", "authorization_code", new AccessTokenRequest.Listener() {
            @Override
            public void onResponse(String response) {
                isListenerOnResponseCalled = true;
            }
        }, new AccessTokenRequest.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    @Test
    public void getParams_checkCorrectKeyMappings() {
        Map<String, String> params = accessTokenRequest.getParams();
        String actualClientId = params.get("client_id");
        String actualClientSecret = params.get("client_secret");
        String actualRedirectUri = params.get("redirect_uri");
        String actualCode = params.get("code");
        String actualGrantType = params.get("grant_type");

        assertThat(actualClientId, equalTo("12345"));
        assertThat(actualClientSecret, equalTo("54321"));
        assertThat(actualRedirectUri, equalTo("https://rtykl525.com/redirect"));
        assertThat(actualCode, equalTo("525"));
        assertThat(actualGrantType, equalTo("authorization_code"));
    }

    @Test
    public void deliverResponse_checkListenerOnResponseCalled() {
        accessTokenRequest.deliverResponse("token");
        assertThat(isListenerOnResponseCalled, equalTo(true));
    }

    @Test
    public void parseNetworkResponse_withValidNetworkResponse() throws JSONException {
        JSONObject dataJson = new JSONObject();
        dataJson.put("access_token", "525");
        byte[] dataBytes = dataJson.toString().getBytes();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html; charset=utf-8");

        Response<String> accessTokenRequestResponse = accessTokenRequest.parseNetworkResponse(new NetworkResponse(dataBytes, headers));
        String actualToken = accessTokenRequestResponse.result;
        String actualContentType = accessTokenRequestResponse.cacheEntry.responseHeaders.get("Content-Type");

        assertThat(actualToken, equalTo("525"));
        assertThat(actualContentType, equalTo("text/html; charset=utf-8"));
    }

}
