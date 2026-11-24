package api;

import config.ConfigReader;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest {
    @Test
    public void healthEndpointReturnsUp() {
        given()
                .baseUri(ConfigReader.baseUrl())
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200);
    }
}
