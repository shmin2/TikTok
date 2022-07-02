package BunkerApi.Steps;

import io.restassured.http.Header;

public class RequestBuilder {

    public static Header getAuthHeader(String token) {
        return new Header("Authorization", "Bearer " + token);
    }
}
