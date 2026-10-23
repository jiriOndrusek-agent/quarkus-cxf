package io.quarkiverse.cxf.test.internal;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

/**
 * Copies {@code qcxf.fips.enabled} to the {@code fips.enabled} system property read by CXF and WSS4J, while the tests
 * are executing.
 * <p>
 * {@code fips.enabled} must not be set earlier: Quarkus resolves the application model of {@code @QuarkusTest}s during
 * test discovery and {@code fips.enabled} would activate the {@code fips} profile of {@code org.apache.cxf:cxf-parent}
 * which adds {@code org.bouncycastle:bc-fips} to the class path.
 */
public class FipsTestExecutionListener implements TestExecutionListener {

    private static final String QCXF_FIPS_ENABLED = "qcxf.fips.enabled";
    private static final String FIPS_ENABLED = "fips.enabled";

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        final String value = System.getProperty(QCXF_FIPS_ENABLED);
        if (value != null) {
            System.setProperty(FIPS_ENABLED, value);
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (System.getProperty(QCXF_FIPS_ENABLED) != null) {
            System.clearProperty(FIPS_ENABLED);
        }
    }
}
