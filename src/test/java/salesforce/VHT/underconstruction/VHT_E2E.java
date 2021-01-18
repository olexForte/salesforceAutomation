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


       HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
       Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
       //Assert.assertTrue(HeaderComponent.ifCountItemInCartEquals(expectedNumberOfItemsInCart), "Wrong number of items in cart");
    }

    @Test(testName = "E2E test 2")
    public void test2(){

        HashMap<String,String> params = dataRepository.getParametersForTest("VHT_E2E");

        // login
        logInApplication();


      //  MainPageComponent.openAddressFromTable(params.get("NAME"),"");

        MainPageComponent.hoverItem(By.xpath("//div[@class='image-container']"));
        click ("//div[@class='image-container']//*[@title='Add To Cart']")

//check message appeared
        ("//span[@class='toastMessage slds-text-heading--small forceActionsText']")

                // check item was added

        //(optional)

        //hover and click X

        // check item was removed


        //check if record open
        System.out.println();
    }
}
