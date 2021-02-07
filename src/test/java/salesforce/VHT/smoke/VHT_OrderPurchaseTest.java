package salesforce.VHT.smoke;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.MainPageComponent;
import components.salesforce.common.OrderCreatedPopUp;
import components.salesforce.vht.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class VHT_OrderPurchaseTest extends BaseUITest {

    private VHTMainPageComponent mainPageComponent = (VHTMainPageComponent) MainPageComponent.getInstance();
    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private VHTAddressListingComponent vHTAddressListingComponent = VHTAddressListingComponent.getInstance();
    private VHTOrderSummaryComponent vHTOrderSummaryComponent = VHTOrderSummaryComponent.getInstance();
  //  private MainPageComponent mainPageComponent = MainPageComponent.getInstance();

    @Test(testName = "Order Purchase test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("OrderPurchase");
        String filter = dataRepository.getContentFromFile("OrderSummaryFilter.json");

        // login
        logIn(false);
        mainPageComponent.openAddressFromTable(params.get("NAME"));
        //check if record open
        Assert.assertEquals(vHTAddressListingComponent.getTitle(), params.get("NAME"),"Expected title was not found");

        //open order summary
        vHTAddressListingComponent.openOrderSummary(params.get("ORDER_SUMMARY_TITLE"), filter);


        //check if record open
        Assert.assertEquals(vHTOrderSummaryComponent.getTitle(), params.get("ORDER_SUMMARY_TITLE"), "Wrong order summary title");

        //count of item in cart
        int expectedNumberOfItemsInCart = headerComponent.getCountItemInCart() + Integer.valueOf(params.get("EXPECTED_QUANTITY_OF_ITEMS"));
        vHTOrderSummaryComponent.startReorder(); // item/items add
        Assert.assertTrue(OrderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open


        headerComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
       Assert.assertEquals(headerComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
    }


}
