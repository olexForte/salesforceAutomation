package utils;

import configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import components.BasePageComponent;
import components.salesforce.common.LoginComponent;
import web.DriverProvider;

import java.lang.reflect.Method;


public class BaseUITest extends BaseTest{

    @BeforeMethod
    public void beforeWithData(Object[] data, Method method) {

        super.beforeWithData(data, method);

        //init threadlocal driver
        try {
            reporter.info("Driver creation");
            if (BasePageComponent.driver.get() == null){
                BasePageComponent.driver.set(DriverProvider.getDriver(reporter.TEST_NAME.get()));
                reporter.info("Driver created " + BasePageComponent.driver.get().hashCode());
            }
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
                .loginAs(ProjectConfiguration.getConfigProperty("DefaultUserName"),
                        ProjectConfiguration.getConfigProperty("DefaultUserPassword"));

    }


    public void logIn(boolean forced){
        if ( ProjectConfiguration.getConfigProperty("LOGGED_IN_DRIVER") == null || forced) {
            logInApplication();
            ProjectConfiguration.setLocalThreadConfigProperty("LOGGED_IN_DRIVER", BasePageComponent.driver().toString());
        }
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
    }

    @AfterTest
    public void closeDriver() throws Exception {
       // BasePage BasePage = new BasePage();
        //close driver
        ProjectConfiguration.removeLocalThreadConfigProperty("LOGGED_IN_DRIVER"); // just in case
        BasePageComponent.driver().quit();
        DriverProvider.closeDriver();

    }
}
