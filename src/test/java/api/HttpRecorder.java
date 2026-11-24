package api;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRecorder implements Filter {
    private static final ThreadLocal<List<String>> EXCHANGES = ThreadLocal.withInitial(ArrayList::new);
    private static final String SECRET_FIELDS = "(\"(?:password|token|accessToken|refreshToken)\"\\s*:\\s*\")[^\"]*(\")";

    @Override
    public Response filter(FilterableRequestSpecification request,
                           FilterableResponseSpecification responseSpec,
                           FilterContext context) {
        Response response = context.next(request, responseSpec);
        EXCHANGES.get().add(describe(request, response));
        return response;
    }

    public static List<String> recorded() {
        return List.copyOf(EXCHANGES.get());
    }

    public static void clear() {
        EXCHANGES.get().clear();
    }

    private static String describe(FilterableRequestSpecification request, Response response) {
        StringBuilder text = new StringBuilder()
                .append(request.getMethod()).append(' ').append(request.getURI()).append('\n')
                .append("> ").append(CorrelationIdFilter.HEADER).append(": ")
                .append(request.getHeaders().getValue(CorrelationIdFilter.HEADER)).append('\n');
        String body = bodyOf(request);
        if (!body.isEmpty()) {
            text.append("> ").append(mask(body)).append('\n');
        }
        return text
                .append("< ").append(response.getStatusLine()).append('\n')
                .append("< Content-Type: ").append(response.getContentType()).append('\n')
                .append("< ").append(mask(response.asString()))
                .toString();
    }

    private static String bodyOf(FilterableRequestSpecification request) {
        Object body = request.getBody();
        if (body == null) {
            return "";
        }
        if (body instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return body.toString();
    }

    private static String mask(String json) {
        return json.replaceAll(SECRET_FIELDS, "$1***$2");
    }
}
