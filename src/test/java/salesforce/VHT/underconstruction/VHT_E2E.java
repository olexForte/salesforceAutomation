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
        String filter = dataRepository.getContentFromFile("OrderSummaryFilter.json");

        // login
        logInApplication();

        MainPageComponent.openAddressFromTable(params.get("NAME"),"");
        //check if record open
        Assert.assertEquals(AddressListingComponent.getTitle(), params.get("NAME"),"Expected title was not found");

        //open order summary
        AddressListingComponent.openOrderSummary(params.get("ORDER_SUMMARY_TITLE"), filter);


        //check if record open
        Assert.assertEquals(OrderSummaryComponent.getTitle(), params.get("ORDER_SUMMARY_TITLE"), "Wrong order summary title");

        //count of item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + Integer.valueOf(params.get("EXPECTED_QUANTITY_OF_ITEMS"));

        OrderSummaryComponent.startReorder(); // item/items add
        Assert.assertTrue(OrderCreatedPopUp.checkIfExist(), "No dialog displayed"); // check if pop-up open


        //TODO ( time for change count to long in page Side )
       // Assert.assertEquals(HeaderComponent.getCountItemInCart(),expectedNumberOfItemsInCart, "Wrong number of items in cart");
        Assert.assertTrue(HeaderComponent.ifCountItemInCartEquals(expectedNumberOfItemsInCart), "Wrong number of items in cart");
    }
}
