package api;

import config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class BaseApi {
    public static final String API_V1 = "/api/v1";

    private BaseApi() {
    }

    public static RequestSpecification apiSpec() {
        return common()
                .setBasePath(API_V1)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification rootSpec() {
        return common().build();
    }

    private static RequestSpecBuilder common() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.baseUrl())
                .setAccept(ContentType.JSON)
                .setConfig(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .defaultCharsetForContentType("UTF-8", ContentType.JSON)))
                .addFilter(new CorrelationIdFilter())
                .addFilter(new HttpRecorder());
    }
}
