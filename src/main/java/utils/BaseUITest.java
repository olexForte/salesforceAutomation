package utils;

import configuration.DataRepository;
import configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import pages.BasePageComponent;
import pages.salesforce.LoginComponent;
import reporting.ReporterManager;
import web.DriverProvider;

import java.lang.reflect.Method;
import java.util.HashMap;


public class BaseUITest extends BaseTest{

    @BeforeMethod
    public void beforeWithData(Object[] data, Method method) {

        super.beforeWithData(data,method);

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

       // LogIn();
        //BasePage.driver().manage().window().maximize();

    }

    /**
     * Login to application with default credentials
     */
    public  void logInApplication() {

        LoginComponent.open(ProjectConfiguration.getConfigProperty("ClientEnvironmentURL"));
        LoginComponent
                .loginAs(ProjectConfiguration.getConfigProperty("DefaultUsername"),
                        ProjectConfiguration.getConfigProperty("DefaultUserPassword"));

    }

    /**
     * Login to application with default credentials
     */
    public  void logInSalesforce() {

        LoginComponent.open(ProjectConfiguration.getConfigProperty("SalesforceURL"));
        LoginComponent
                .loginAs(ProjectConfiguration.getConfigProperty("DefaultSalesforceUserName"),
                        ProjectConfiguration.getConfigProperty("DefaultSalesforceUserPassword"));

    }

    @AfterMethod
    public void endTest(ITestResult testResult) throws Exception {

        super.endTest(testResult);
       // BasePage BasePage = new BasePage();
        //close driver
        BasePageComponent.driver().quit();
        DriverProvider.closeDriver();

    }
}
