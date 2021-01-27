package utils;

import components.salesforce.common.AnotherSearchCompnent;
import components.salesforce.common.LoginComponent;
import components.salesforce.common.SearchComponent;
import configuration.DataRepository;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporting.ReporterManager;

import java.lang.reflect.Method;
import java.util.HashMap;


public class BaseTest{

    public ReporterManager reporter;
    public DataRepository dataRepository;

    public HashMap<String, String> params;

    //TODO factory???
//    public LoginComponent loin;
//    public SearchComponent seach;
//
//    private void createAllComponents() {
//        seach = new SearchComponent();
//        if ()
//            seach = new AnotherSearchCompnent();
//        ...
//    }

    @BeforeMethod
    public void beforeWithData(Object[] data, Method method) {

        //init reporter
        reporter = ReporterManager.Instance;
        reporter.startReporting(method, data);

        //init data
        dataRepository = DataRepository.Instance;

        //TODO params = dataRepository.getParametersForTest();

        //createAllComponents();
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
