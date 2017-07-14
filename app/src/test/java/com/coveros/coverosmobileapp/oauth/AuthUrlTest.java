package com.coveros.coverosmobileapp.oauth;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Maria Kim
 */

public class AuthUrlTest {

    @Test
    public void toString_checkUrlIsCorrect() {

        AuthUrl authUrl = new AuthUrl("https://rtykl525.com/authorize", "12345", "https://rtykl525.com/redirect", "code");

        String actualAuthUrl = authUrl.toString();
        assertThat(actualAuthUrl, equalTo("https://rtykl525.com/authorize?client_id=12345&redirect_uri=https://rtykl525.com/redirect&response_type=code"));

    }


}
