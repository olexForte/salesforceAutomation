package salesforce.VHT.underconstruction;

import configuration.DataRepository;
import org.testng.annotations.Test;
import pages.salesforce.MainPageComponent;
import utils.BaseUITest;

import java.util.HashMap;

public class VHT_E2E extends BaseUITest {

    HashMap<String,String> params = dataRepository.getParametersForTest("VHT_E2E");

    @Test(testName = "E2E test")
    public void test(){

        // login
        logInApplication();

        MainPageComponent.openAddressFromTable(params.get("NAME"));

        //AddressListingComponent.checkTitle(params.get("NAME"));

        AddressListingComponent.openTab(params.get("TAB_NAME"))


        BasePageComponent.findElement(By.xpath("//label[.='Date']/following::div[1]/input")).clear();
        BasePageComponent.findElement(By.xpath("//label[.='Date']/following::div[1]/input")).sendKeys("12/11/2021"); //1/18/2020
        Actions a = new Actions(BasePageComponent.driver()); a.sendKeys(Keys.TAB).build().perform();


        00000100


                orders table
        //td[1]/span[@class='record-link'][.='%s']


        OrderSummaryCompponent
        "//span[text()='My Order']/following::span[1]")

        //button[ = START REORDER]

dialogboxcoponent
       h2 [] All items were added to cart]
        button[title="CONTINUE SHOPPING"]


                HeaderComponent
        "//div[@class='cartButton']

                cartcoponent

        //button Proceed To Checkout

        System.out.println();

    }
}
