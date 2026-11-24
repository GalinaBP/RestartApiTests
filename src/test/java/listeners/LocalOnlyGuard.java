package listeners;

import base.Groups;
import config.ConfigReader;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.Arrays;

public class LocalOnlyGuard implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (isLocalOnly(method) && !ConfigReader.localOnlyAllowed()) {
            throw new SkipException("Local-only: local.only.allowed is false for env="
                    + System.getProperty("env", "local"));
        }
    }

    private static boolean isLocalOnly(IInvokedMethod method) {
        boolean onMethod = method.isTestMethod()
                && Arrays.asList(method.getTestMethod().getGroups()).contains(Groups.LOCAL_ONLY);
        Class<?> testClass = method.getTestMethod().getRealClass();
        Test onClass = testClass.getAnnotation(Test.class);
        boolean onTheClass = onClass != null && Arrays.asList(onClass.groups()).contains(Groups.LOCAL_ONLY);
        return onMethod || onTheClass;
    }
}
