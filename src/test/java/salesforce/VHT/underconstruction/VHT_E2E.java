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

        MainPageComponent.openAddressFromTable(params.get("NAME"));
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

        HashMap<String,String> params = dataRepository.getParametersForTest("VHT_E2E(2)");

        // login
        logInApplication();
        //open page proof Photographs
        MainPageComponent.openProofPhotographsFromTable(params.get("NAME"));

        //test ADD
        //count of expected item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + 1;

        //add image to cart and check if message is correct
        ProofPhotographsComponent.addPictureToCart(params.get("PICTURE_NAME"));
        String actualMessage = ProofPhotographsComponent.getMessageFromPage();
        String expectedMessage = params.get("TEXT_ADD_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after added
        HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
        //check if button remove exist and button add isn`t exist
        //too long
        Assert.assertTrue((!ProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && ProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));

        //test REMOVE
        //count of expected item in cart
        expectedNumberOfItemsInCart=expectedNumberOfItemsInCart-1;

        //remove image from cart and check if message is correct
        ProofPhotographsComponent.removePictureFromCart(params.get("PICTURE_NAME"));

        actualMessage = ProofPhotographsComponent.getMessageFromPage();
        expectedMessage = params.get("TEXT_REMOVE_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after remove
        HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");

        Assert.assertTrue((ProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && !ProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));
    }
}
