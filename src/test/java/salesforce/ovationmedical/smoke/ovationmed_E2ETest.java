package salesforce.ovationmedical.smoke;

import components.salesforce.common.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class ovationmed_E2ETest extends BaseUITest {

    @Test(testName = "E2E test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("E2E");

        // login
        logInApplication();

        HeaderComponent.searchForProduct(params.get("PRODUCT_NAME"));
        MainPageComponent.openItemFromTable(params.get("PRODUCT_NAME"));
        //check if record open
        Assert.assertEquals(ProductDetailsComponent.getTitle(), params.get("PRODUCT_NAME"),"Expected product was not open");
        ProductDetailsComponent.addToCart(); // item/items add

        //count of item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + 1;
        Assert.assertTrue(OrderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open

        HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");

        OrderCreatedPopUp.clickOnViewCartButton();
        Assert.assertTrue(CartPageComponent.isItemInCart(params.get("PRODUCT_NAME")), "Item was not found in cart");

    }


}
