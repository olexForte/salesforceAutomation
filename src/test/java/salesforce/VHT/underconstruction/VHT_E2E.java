package salesforce.VHT.underconstruction;

import org.testng.Assert;
import org.testng.annotations.Test;
import components.salesforce.*;
import utils.BaseUITest;

import java.util.HashMap;

public class VHT_E2E extends BaseUITest {

    @Test(testName = "E2E test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("VHT_E2E");

        // login
        logInApplication();

        MainPageComponent.openAddressFromTable(params.get("NAME"));
        //check if record open
        Assert.assertEquals(AddressListingComponent.getTitle(), params.get("NAME"),"Expected title was not found");

        //open order summary
        String filter = "{\"dateTo\":\"01/04/2020\"}";
        AddressListingComponent.openOrderSummary(params.get("ORDER_SUMMARY_TITLE"), filter);
//        AddressListingComponent.openTab(params.get("TAB_NAME"));
//        AddressListingComponent.searchByDate(params.get("EXPECTED_DATE"));
//        AddressListingComponent.clickOnOrderTitle(params.get("ORDER_SUMMARY_TITLE"));

        //check if record open
        Assert.assertEquals(OrderSummaryComponent.getTitle(), params.get("ORDER_SUMMARY_TITLE"), "Wrong order summary title");

        //count of item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + Integer.valueOf(params.get("EXPECTED_QUANTITY_OF_ITEMS"));

        OrderSummaryComponent.startReorder(); //1 item add
        Assert.assertTrue(OrderCreatedPopUp.checkIfExist(), "No dialog displayed"); // check if pop-up open
        Assert.assertEquals(expectedNumberOfItemsInCart , HeaderComponent.getCountItemInCart(), "Wrong number of items in cart");

    }
}
