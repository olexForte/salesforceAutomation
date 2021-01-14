package salesforce;

import configuration.ProjectConfiguration;
import org.testng.Assert;
import components.BasePageComponent;
import components.salesforce.LoginComponent;
import utils.BaseTest;
import org.testng.annotations.Test;

public class VHTtests extends BaseTest {

    String url = ProjectConfiguration.getConfigProperty("URL");

    @Test()
    public void  PositiveLogIn(){
       BasePageComponent.open(url);

        LoginComponent login = new LoginComponent();

        login.loginAs("yurii.holoiad13@fortegrp.com","252846173922");
        Assert.assertEquals(BasePageComponent.driver().getTitle(),"Home");
    }

}
