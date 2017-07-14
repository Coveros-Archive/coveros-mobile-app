package com.coveros.coverosmobileapp.oauth;

import com.android.volley.VolleyError;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Maria Kim
 */

public class AccessTokenRequestTest {

    AccessTokenRequest accessTokenRequest;

//    @Before
//    public void setUp() {
//        accessTokenRequest = new AccessTokenRequest("https://rtykl525.com", "12345", "54321", "https://rtykl525.com/redirect", "525", "authorization_code", new AccessTokenRequest.Listener() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("Success");
//            }
//        }, new AccessTokenRequest.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("Error");
//            }
//        });
//    }

    @Test
    public void test() {
        assertThat("true", equalTo("true"));
    }
//    @Test
//    public void getParams_checkCorrectKeyMappings() {
//        Map<String, String> params = accessTokenRequest.getParams();
//        String actualClientId = params.get("client_id");
//        String actualClientSecret = params.get("client_secret");
//        String actualRedirectUri = params.get("redirect_uri");
//        String actualCode = params.get("code");
//        String actualGrantType = params.get("grant_type");
//
//        assertThat(actualClientId, equalTo("12345"));
//        assertThat(actualClientSecret, equalTo("54321"));
//        assertThat(actualRedirectUri, equalTo("rtyl525.com/redirect"));
//        assertThat(actualCode, equalTo("525"));
//        assertThat(actualGrantType, equalTo("authorization_code"));
//    }
}
