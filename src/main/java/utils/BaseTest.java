package utils;

import configuration.DataRepository;
import configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.BasePageComponent;
import configuration.LocatorsRepository;
import pages.salesforce.LoginComponent;
import reporting.ReporterManager;
import web.DriverProvider;

import java.lang.reflect.Method;


public class BaseTest{

    public ReporterManager reporter;
    public DataRepository dataRepository;

    @BeforeMethod
    public void beforeWithData(Object[] data, Method method) {

        //init reporter
        reporter = ReporterManager.Instance;
        reporter.startReporting(method, data);

        //init data
        dataRepository = DataRepository.Instance;
    }

    @AfterMethod
    public void endTest(ITestResult testResult) throws Exception {

        // close reporter
        reporter.stopReporting(testResult);

    }

    @AfterSuite(alwaysRun = true)
    public void flushReporter() {
        reporter.closeReporter();
    }
}
