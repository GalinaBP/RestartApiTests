package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EligibilityApiClient {
    public static final String CHECK = "/eligibility/check";

    public Response check(Object body) {
        return given()
                .spec(BaseApi.apiSpec())
                .body(body)
                .when()
                .post(CHECK);
    }
}
