package com.coveros.coverosmobileapp.oauth;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Running this as an instrumented test because running as a unit test gives a UnsatisfiedLinkError
 * @author Maria Kim
 */

public class AccessTokenRequestInstrumentedTest {

    AccessTokenRequest accessTokenRequest;
    boolean isListenerOnResponseCalled = false;

    @Mock
    NetworkResponse networkResponse;

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
    public void parseNetworkResponse_withValidNetworkResponse() {
        byte[] data = {'a', 'b', 'c'};
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html; charset=utf-8");
        headers.put("access_token", "12345");
        when(networkResponse.data).thenReturn(data);
        when(networkResponse.headers).thenReturn(headers);

        Response<String> accessTokenRequestResponse = accessTokenRequest.parseNetworkResponse(networkResponse);
    }

}
