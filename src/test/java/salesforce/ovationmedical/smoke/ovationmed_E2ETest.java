package salesforce.ovationmedical.smoke;

import components.salesforce.common.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class ovationmed_E2ETest extends BaseUITest {

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private ProductDetailsComponent productDetailsComponent = ProductDetailsComponent.getInstance();
    private CartPageComponent cartPageComponent = CartPageComponent.getInstance();
    private MainPageComponent mainPageComponent = MainPageComponent.getInstance();

    @Test(testName = "E2E test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("E2E");
        // login
        logIn(false);

        headerComponent.searchForProduct(params.get("PRODUCT_NAME"));
        mainPageComponent.openItemFromTable(params.get("PRODUCT_NAME"));
        //check if record open
        Assert.assertEquals(productDetailsComponent.getTitle(), params.get("PRODUCT_NAME"),"Expected product was not open");
        productDetailsComponent.addToCart(); // item/items add

        //count of item in cart
        int expectedNumberOfItemsInCart = headerComponent.getCountItemInCart() + 1;
        Assert.assertTrue(OrderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open

        headerComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(headerComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");

        OrderCreatedPopUp.clickOnViewCartButton();
        Assert.assertTrue(cartPageComponent.isItemInCart(params.get("PRODUCT_NAME")), "Item was not found in cart");
    }
}
