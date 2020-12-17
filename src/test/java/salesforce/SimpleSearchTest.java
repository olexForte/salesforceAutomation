package salesforce;

import configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasePageComponent;
import pages.salesforce.LoginComponent;
import utils.BaseTest;

/**
 */
public class SimpleSearchTest extends BaseTest {

    String username = "";//DataRepository.get();
    String password = "";//DataRepository.get();
    String url = ProjectConfiguration.getConfigProperty("URL");

    @Test()
    public void Search(){

        BasePageComponent.open(url);

        LoginComponent login = new LoginComponent();
        login.loginAs(username, "");

        Assert.assertTrue(BasePageComponent.isTextPresent(""));
    }

}
