package api;

import io.restassured.response.Response;
import models.dto.ForgotPasswordRequest;
import models.dto.LoginRequest;
import models.dto.RefreshRequest;
import models.dto.RegisterRequest;
import models.dto.ResetPasswordRequest;

import static io.restassured.RestAssured.given;

public class AuthApiClient {
    public static final String REGISTER = "/auth/register";
    public static final String LOGIN = "/auth/login";
    public static final String REFRESH = "/auth/refresh";
    public static final String FORGOT_PASSWORD = "/auth/forgot-password";
    public static final String RESET_PASSWORD = "/auth/reset-password";
    public static final String ME = "/auth/me";

    public Response register(String email, String password) {
        return register(new RegisterRequest(email, password));
    }

    public Response register(Object body) {
        return post(REGISTER, body);
    }

    public Response login(String email, String password) {
        return login(new LoginRequest(email, password));
    }

    public Response login(Object body) {
        return post(LOGIN, body);
    }

    public Response refresh(String refreshToken) {
        return refresh(new RefreshRequest(refreshToken));
    }

    public Response refresh(Object body) {
        return post(REFRESH, body);
    }

    public Response forgotPassword(String email) {
        return forgotPassword(new ForgotPasswordRequest(email));
    }

    public Response forgotPassword(Object body) {
        return post(FORGOT_PASSWORD, body);
    }

    public Response resetPassword(String token, String password) {
        return resetPassword(new ResetPasswordRequest(token, password));
    }

    public Response resetPassword(Object body) {
        return post(RESET_PASSWORD, body);
    }

    public Response me(String accessToken) {
        return meWithAuthorization(bearer(accessToken));
    }

    public Response meWithAuthorization(String authorizationHeaderValue) {
        return given()
                .spec(BaseApi.apiSpec())
                .header("Authorization", authorizationHeaderValue)
                .when()
                .get(ME);
    }

    public Response meWithoutToken() {
        return given()
                .spec(BaseApi.apiSpec())
                .when()
                .get(ME);
    }

    public static String bearer(String accessToken) {
        return "Bearer " + accessToken;
    }

    private Response post(String path, Object body) {
        return given()
                .spec(BaseApi.apiSpec())
                .body(body)
                .when()
                .post(path);
    }
}
