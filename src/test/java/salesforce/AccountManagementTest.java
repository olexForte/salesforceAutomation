package salesforce;

import components.salesforce.common.AccountManagementComponent;
import components.salesforce.common.HeaderComponent;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class AccountManagementTest extends BaseUITest {



    private AccountManagementComponent accountManagementComponent = new AccountManagementComponent();
    private HeaderComponent headerComponent = new HeaderComponent();

    @Test(testName = "Accaount Management test")
    public void accountManagementTest(){
        logInApplication();
        HashMap<String,String> params = dataRepository.getParametersForTest("AccountInformationTest");
        headerComponent.openItemFromUserMenu(params.get("PAGE_NAME"));

    }


}
