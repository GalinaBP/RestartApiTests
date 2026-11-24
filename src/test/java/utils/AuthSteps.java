package utils;

import api.AuthApiClient;
import config.ConfigReader;
import io.restassured.response.Response;
import models.dto.TokenResponse;
import org.testng.Assert;
import org.testng.SkipException;

public final class AuthSteps {
    private static final AuthApiClient AUTH = new AuthApiClient();

    private AuthSteps() {
    }

    public static TestUser registerNewUser() {
        if (!ConfigReader.localOnlyAllowed()) {
            throw new SkipException("registerNewUser writes an account that can never be deleted: local only");
        }
        String email = TestData.uniqueEmail();
        String password = Passwords.VALID;
        Response response = AUTH.register(email, password);
        Assert.assertEquals(response.statusCode(), 201, "Setup: register " + email + " -> " + response.asString());
        return toUser(email, password, response);
    }

    public static TestUser loginSmokeUser() {
        String email = ConfigReader.smokeEmail();
        String password = ConfigReader.smokePassword();
        Response response = AUTH.login(email, password);
        Assert.assertEquals(response.statusCode(), 200, "Setup: login " + email + " -> " + response.statusLine());
        return toUser(email, password, response);
    }

    private static TestUser toUser(String email, String password, Response response) {
        TokenResponse tokens = response.as(TokenResponse.class);
        return new TestUser(email, password, tokens.accessToken(), tokens.refreshToken());
    }
}
