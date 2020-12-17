package utils;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.BasePage;
import pages.BasePageComponent;
import reporting.ReporterManager;
import web.DriverProvider;

import java.lang.reflect.Method;


public class BaseTest{

    ReporterManager reporter;

    @BeforeMethod
    public void beforeWithData(Object[] data, Method method) {

        //init reporter
        reporter = ReporterManager.Instance;
        reporter.startReporting(method, data);

        //init threadlocal driver
        try {
            reporter.info("Driver creation");
            BasePageComponent.driver.set(DriverProvider.getDriver(reporter.TEST_NAME.get()));
            //reporter.info("Driver created " + BasePage.driver.get().hashCode());
        }catch (Exception e){
            reporter.fail("Before test failure during Driver creation", e);
            reporter.stopReporting();
            reporter.closeReporter();
            Assert.fail();
        }

        //BasePage.driver().manage().window().maximize();

    }

    @AfterMethod
    public void endTest(ITestResult testResult) throws Exception {

        // close reporter
        reporter.stopReporting(testResult);

        //close driver
        BasePage.driver().quit();
        DriverProvider.closeDriver();

    }

    @AfterSuite(alwaysRun = true)
    public void flushReporter() {
        reporter.closeReporter();
    }
}
