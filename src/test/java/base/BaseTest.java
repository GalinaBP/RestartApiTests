package base;

import api.AuthApiClient;
import api.EligibilityApiClient;
import api.HealthApiClient;
import listeners.FailureReportListener;
import listeners.LocalOnlyGuard;
import org.testng.annotations.Listeners;

@Listeners({FailureReportListener.class, LocalOnlyGuard.class})
public abstract class BaseTest {
    protected final AuthApiClient auth = new AuthApiClient();
    protected final EligibilityApiClient eligibility = new EligibilityApiClient();
    protected final HealthApiClient health = new HealthApiClient();
}
