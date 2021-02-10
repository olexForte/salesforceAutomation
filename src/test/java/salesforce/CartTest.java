package salesforce;

import components.salesforce.common.*;
import org.apache.xmlbeans.soap.SchemaWSDLArrayType;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class CartTest extends BaseUITest {


    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private ProductDetailsComponent productDetailsComponent = ProductDetailsComponent.getInstance();
    private CartPageComponent cartPageComponent = CartPageComponent.getInstance();
    private SearchResultComponent searchResultComponent = SearchResultComponent.getInstance();
    private OrderCreatedPopUp orderCreatedPopUp = OrderCreatedPopUp.getInstance();


    @Test(testName = "Cart items test")
    public void ContactSupportTest() {

        logIn(false);

        HashMap<String,String> params = dataRepository.getParametersForTest("CartTest");
        // login
        logIn(false);

        //find product
        headerComponent.findByQuery(params.get("PRODUCT_NAME"));
        //add item to cart
        searchResultComponent.addItemToCart(params.get("PRODUCT_NAME"));
        //check if popup displayed
        Assert.assertTrue(orderCreatedPopUp.isPopupDisplayed(), "No dialog displayed");
        //click button "view in cart"
        orderCreatedPopUp.clickOnViewCartButton();
        //check if item is in cart
        Assert.assertTrue(cartPageComponent.isItemInCart(params.get("PRODUCT_NAME")), "Item was not found in cart");
        //get final price with item
        double actualPrice=Double.valueOf(cartPageComponent.getFinalPrice());
        //get final price without item
        double expectedPrice=actualPrice-Double.valueOf(params.get("ITEM_PRICE"));
        //remove item from cart
        cartPageComponent.removeItem(params.get("PRODUCT_NAME"));
        //wait for price will be change
        cartPageComponent.waitForFinalPrice(expectedPrice);
        //assert actual price and expected
        Assert.assertEquals(expectedPrice,Double.valueOf(cartPageComponent.getFinalPrice()),"Wrong price in cart");
    }

}
