package com.coveros.coverosmobileapp.oauth;

/**
 * Produces full authorization URL with parameters at which the user logs in.
 * @author Maria Kim
 */

public class AuthUrl {

    private String endpoint;
    private String clientId;
    private String redirectUri;
    private String responseType;

    private final String CLIENT_ID_KEY = "client_id";
    private final String REDIRECT_URI_KEY = "redirect_uri";
    private final String RESPONSE_TYPE_KEY = "response_type";

    /**
     * @param endpoint    auth url provided by WP OAuth Server
     * @param clientId    client id provided by WP
     * @param redirectUri    redirect uri that contains the authorization code that the WebView in OAuthLoginActivity watches for
     * @param responseType    type of response requested
     */
    AuthUrl(String endpoint, String clientId, String redirectUri, String responseType) {
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.responseType = responseType;
    }

    public String toString() {
        return endpoint + "?" + CLIENT_ID_KEY + "=" + clientId + "&" + REDIRECT_URI_KEY + "=" + redirectUri + "&" + RESPONSE_TYPE_KEY + "=" + responseType;
    }

}
