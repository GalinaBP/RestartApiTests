package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HealthApiClient {
    public static final String HEALTH = "/actuator/health";
    public static final String LIVENESS = HEALTH + "/liveness";
    public static final String READINESS = HEALTH + "/readiness";

    public Response health() {
        return get(HEALTH);
    }

    public Response liveness() {
        return get(LIVENESS);
    }

    public Response readiness() {
        return get(READINESS);
    }

    private Response get(String path) {
        return given()
                .spec(BaseApi.rootSpec())
                .when()
                .get(path);
    }
}
