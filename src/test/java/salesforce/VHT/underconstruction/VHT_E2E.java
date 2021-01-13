package salesforce.VHT.underconstruction;

import configuration.DataRepository;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasePageComponent;
import pages.salesforce.*;
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
        Assert.assertTrue(AddressListingComponent.checkTitle(params.get("NAME")));

        AddressListingComponent.openTab(params.get("TAB_NAME"));
        AddressListingComponent.searchByData("1/4/2020");
        AddressListingComponent.openOrderSummary(params.get("Order_Summary_Title"));

        //check if record open
        Assert.assertTrue(OrderSummaryComponent.checkTitle(params.get("Order_Summary_Title")));

        //count of item in cart
        int countItemInCart =HeaderComponent.getCountItemInCart();

        OrderSummaryComponent.startReorder(); //1 item add
        Assert.assertTrue(OrderCreatedPopUp.cleckIfExist()); // check if pop-up open
        Assert.assertTrue(countItemInCart==HeaderComponent.getCountItemInCart()? false : true);

//    timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
//        00000100
//
//
//                orders table
//        //td[1]/span[@class='record-link'][.='00000100']
//
//
//        OrderSummaryCompponent
//        "//span[text()='My Order']/following::span[1]")
//
//        //button[ = START REORDER]
//
//dialogboxcoponent
//       h2 [] All items were added to cart]
//        button[title="CONTINUE SHOPPING"]
//
//
//                HeaderComponent
//        "//div[@class='cartButton']
//
//                cartcoponent
//
//        //button Proceed To Checkout


        System.out.println();

    }
}
