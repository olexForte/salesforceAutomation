package salesforce;

import components.salesforce.common.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class E2ETest extends BaseUITest {

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private ProductDetailsComponent productDetailsComponent = ProductDetailsComponent.getInstance();
    private CartPageComponent cartPageComponent = CartPageComponent.getInstance();
    private SearchResultComponent searchResultComponent = SearchResultComponent.getInstance();
    private OrderCreatedPopUp orderCreatedPopUp = OrderCreatedPopUp.getInstance();

    @Test(testName = "E2E test")
    public void E2ETest(){

        HashMap<String,String> params = dataRepository.getParametersForTest("E2ETest");
        // login
        logIn(false);

        headerComponent.findByQuery(params.get("PRODUCT_NAME"));
        searchResultComponent.openItemFromTable(params.get("PRODUCT_NAME"));
        //check if record open
        Assert.assertEquals(productDetailsComponent.getTitle(), params.get("PRODUCT_NAME"),"Expected product was not open");
        productDetailsComponent.addToCart(); // item/items add

        //count of item in cart
        int expectedNumberOfItemsInCart = headerComponent.getCountItemInCart() + 1;
        Assert.assertTrue(orderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open
        orderCreatedPopUp.clickOnViewCartButton();

        headerComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(headerComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
        Assert.assertTrue(cartPageComponent.isItemInCart(params.get("PRODUCT_NAME")), "Item was not found in cart");
    }
}
