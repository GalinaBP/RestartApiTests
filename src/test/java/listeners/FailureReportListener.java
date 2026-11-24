package listeners;

import api.HttpRecorder;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.List;

public class FailureReportListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        List<String> exchanges = HttpRecorder.recorded();
        StringBuilder report = new StringBuilder()
                .append("\n===== HTTP calls of ").append(result.getMethod().getQualifiedName())
                .append(" (").append(exchanges.size()).append(") =====\n");
        for (String exchange : exchanges) {
            report.append(exchange).append("\n-----\n");
        }
        System.out.println(report);
        HttpRecorder.clear();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        HttpRecorder.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        HttpRecorder.clear();
    }
}
