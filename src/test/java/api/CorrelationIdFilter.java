package api;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.UUID;

public class CorrelationIdFilter implements Filter {
    public static final String HEADER = "X-Correlation-Id";
    private static final int MAX_LENGTH = 64;

    @Override
    public Response filter(FilterableRequestSpecification request,
                           FilterableResponseSpecification response,
                           FilterContext context) {
        if (!request.getHeaders().hasHeaderWithName(HEADER)) {
            request.header(HEADER, newId());
        }
        return context.next(request, response);
    }

    public static String newId() {
        String suffix = "-" + UUID.randomUUID().toString().substring(0, 8);
        String name = currentTestName().replaceAll("[^A-Za-z0-9._-]", "_");
        int room = MAX_LENGTH - suffix.length();
        return (name.length() > room ? name.substring(0, room) : name) + suffix;
    }

    private static String currentTestName() {
        ITestResult result = Reporter.getCurrentTestResult();
        return result == null ? "no-test" : result.getMethod().getMethodName();
    }
}
