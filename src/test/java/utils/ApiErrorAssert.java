package utils;

import api.CorrelationIdFilter;
import io.restassured.response.Response;
import models.dto.ApiError;
import models.dto.ApiFieldError;
import org.testng.Assert;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public final class ApiErrorAssert {

    private ApiErrorAssert() {
    }

    public static ApiError assertApiError(Response response, int status, String code, String message) {
        Assert.assertEquals(response.statusCode(), status, "HTTP status");
        String contentType = response.contentType();
        Assert.assertTrue(contentType != null && contentType.startsWith("application/json"),
                "Content-Type should be JSON but was " + contentType);

        ApiError error = response.as(ApiError.class);
        Assert.assertEquals(error.status(), Integer.valueOf(status), "status field");
        Assert.assertEquals(error.code(), code, "code");
        Assert.assertEquals(error.message(), message, "message");
        assertIsInstant(error.timestamp());
        Assert.assertTrue(error.path() != null && error.path().startsWith("/"), "path was " + error.path());
        Assert.assertEquals(error.correlationId(), response.header(CorrelationIdFilter.HEADER),
                "correlationId must equal the " + CorrelationIdFilter.HEADER + " header");
        return error;
    }

    public static void assertFieldErrors(ApiError error, String field, String... expectedMessages) {
        Assert.assertNotNull(error.fieldErrors(), "fieldErrors is missing");
        List<String> actual = error.fieldErrors().stream()
                .filter(fieldError -> field.equals(fieldError.field()))
                .map(ApiFieldError::message)
                .sorted()
                .toList();
        List<String> expected = Arrays.stream(expectedMessages).sorted().toList();
        Assert.assertEquals(actual, expected, "messages for field '" + field + "'");
    }

    public static void assertNoFieldErrors(ApiError error) {
        Assert.assertNull(error.fieldErrors(), "fieldErrors should be absent");
    }

    private static void assertIsInstant(String timestamp) {
        Assert.assertNotNull(timestamp, "timestamp");
        try {
            Instant.parse(timestamp);
        } catch (DateTimeParseException e) {
            Assert.fail("timestamp is not an ISO-8601 instant: " + timestamp);
        }
    }
}
