package salesforce.VHT.smoke;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.MainPageComponent;
import components.salesforce.common.OrderCreatedPopUp;
import components.salesforce.vht.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class VHT_OrderPurchaseTest extends BaseUITest {

    private MainPageComponent mainPageComponent = MainPageComponent.getInstance();
  //  private MainPageComponent mainPageComponent = MainPageComponent.getInstance();
  //  private MainPageComponent mainPageComponent = MainPageComponent.getInstance();

    @Test(testName = "Order Purchase test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("OrderPurchase");
        String filter = dataRepository.getContentFromFile("OrderSummaryFilter.json");

        // login
        logInApplication();
        mainPageComponent.op
        mainPageComponent.openAddressFromTable(params.get("NAME"));
        //check if record open
        Assert.assertEquals(VHTAddressListingComponent.getTitle(), params.get("NAME"),"Expected title was not found");

        //open order summary
        VHTAddressListingComponent.openOrderSummary(params.get("ORDER_SUMMARY_TITLE"), filter);


        //check if record open
        Assert.assertEquals(VHTOrderSummaryComponent.getTitle(), params.get("ORDER_SUMMARY_TITLE"), "Wrong order summary title");

        //count of item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + Integer.valueOf(params.get("EXPECTED_QUANTITY_OF_ITEMS"));
        VHTOrderSummaryComponent.startReorder(); // item/items add
        Assert.assertTrue(OrderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open


       HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
       Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
    }


}
