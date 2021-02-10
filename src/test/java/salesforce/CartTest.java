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
     //   searchSomeProduct
        //add item to cart
        //open cart
        //get Price
        //remove item
        //get price
        //update page
        //get price



        HashMap<String,String> params = dataRepository.getParametersForTest("CartTest");
        // login
        logIn(false);

        headerComponent.findByQuery(params.get("PRODUCT_NAME"));

        searchResultComponent.addItemToCart(params.get("PRODUCT_NAME"));
        Assert.assertTrue(orderCreatedPopUp.isPopupDisplayed(), "No dialog displayed"); // check if pop-up open
        orderCreatedPopUp.clickOnViewCartButton();
        Assert.assertTrue(cartPageComponent.isItemInCart(params.get("PRODUCT_NAME")), "Item was not found in cart");
        double actualPrice=Double.valueOf(cartPageComponent.getFinalPrice());
        double expectedPrice=actualPrice-Double.valueOf(params.get("ITEM_PRICE"));
        cartPageComponent.removeItem(params.get("PRODUCT_NAME"));
        cartPageComponent.waitForFinalPrice(expectedPrice);
        Assert.assertEquals(expectedPrice,Double.valueOf(cartPageComponent.getFinalPrice()));
    }

}
