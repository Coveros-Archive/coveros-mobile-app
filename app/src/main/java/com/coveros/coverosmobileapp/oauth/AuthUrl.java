package com.coveros.coverosmobileapp.oauth;

/**
 * @author Maria Kim
 */

public class AuthUrl {

    private String endpoint;
    private String clientId;
    private String redirectUri;
    private String responseType;

    private final String CLIENT_ID_KEY = "client_id";
    private final String REDIRECT_URL_KEY = "redirect_url";
    private final String RESPONSE_TYPE_KEY = "response_type";

    AuthUrl(String endpoint, String clientId, String redirectUri, String responseType, String scope) {
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.responseType = responseType;
    }

    public String toString() {
        return endpoint + "?" + CLIENT_ID_KEY + "=" + clientId + "&" + REDIRECT_URL_KEY + "=" + redirectUri + "&" + RESPONSE_TYPE_KEY + "=" + responseType;
    }

}