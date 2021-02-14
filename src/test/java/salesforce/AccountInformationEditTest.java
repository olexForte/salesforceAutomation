package salesforce;

import components.salesforce.common.AccountInformationComponent;
import components.salesforce.common.HeaderComponent;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class AccountInformationEditTest extends BaseUITest {

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private AccountInformationComponent accountInformationComponent = AccountInformationComponent.getInstance();

    @Test(testName = "Account information edit test")
    public void AccountInformationEditTest(){
        HashMap<String,String> params = dataRepository.getParametersForTest("AccountInformationTest");

        logIn(false);
        headerComponent.openItemFromUserMenu(params.get("PAGE_NAME"));
        HashMap<String,String> expectedData = accountInformationComponent.setFields(params.get("EXPECTED_FIELD"));
        accountInformationComponent.reloadPage();
        HashMap<String,String> actualData =accountInformationComponent.getFields(params.get("EXPECTED_FIELD"));
        Assert.assertEquals(actualData,expectedData,"Fields is incorrect");
    }
}
