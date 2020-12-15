package salesforce;

import configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.GoogleSearchPage;
import pages.LoginPage;
import utils.BaseTest;
import utils.Tools;

/**
 */
public class SimpleSearchTest extends BaseTest {

    String username = "";
    String password = "";
    String url = ProjectConfiguration.getConfigProperty("URL");

    @Test()
    public void Search(){

        LoginPage login = new LoginPage();
        login.open(url);
        login.loginAs(username, password);

        Assert.assertTrue(login.isTextPresent(""));
    }

}
