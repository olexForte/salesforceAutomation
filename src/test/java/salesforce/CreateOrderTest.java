package salesforce;

import components.salesforce.common.*;
import entities.OrderItem;
import entities.ProductItem;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseComparator;
import utils.BaseUITest;
import utils.OrdersComparator;
import utils.TypesOfComparison;

import java.util.HashMap;

public class CreateOrderTest extends BaseUITest {


    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private CartPageComponent cartPageComponent = CartPageComponent.getInstance();
    private SearchResultComponent searchResultComponent = SearchResultComponent.getInstance();
    private ProductDetailsComponent productDetailsComponent = ProductDetailsComponent.getInstance();
    private OrderCreatedPopUp orderCreatedPopUp = OrderCreatedPopUp.getInstance();


    @Test(testName = "Create order and compare result") // can be dataProvider
    public void createOrderTest(){
        HashMap<String,String> params = dataRepository.getParametersForTest("CreateOrderTest");
        String order=params.get("EXPECTED_ORDER");
        OrderItem expectedOrder = OrderItem.toObject(order);// new OrderItem(order);
        logIn(false);
        int expectedCartCount =headerComponent.getCountItemInCart();

        TypesOfComparison types = new TypesOfComparison(true,false,false);



        for (ProductItem item: expectedOrder.products ){

            headerComponent.findByQuery(item.getName());
            searchResultComponent.openItemFromTable(item.getName());
            productDetailsComponent.addToCart(Integer.parseInt(item.getCount()));
            orderCreatedPopUp.clickOnContinueShopping();
            expectedCartCount=expectedCartCount+Integer.parseInt(item.getCount());
        }

        headerComponent.openCart();
        headerComponent.waitForNumberOfItemsInCart(Integer.parseInt(expectedOrder.getCount()));
        int actualCount=headerComponent.getCountItemInCart();
        Assert.assertEquals(Integer.parseInt(expectedOrder.getCount()),actualCount);

        float finalPrice=cartPageComponent.getFinalPrice();
        Assert.assertEquals(Float.valueOf(expectedOrder.getSummaryPrice()),finalPrice);
        cartPageComponent.proceedToCheckout();

//TODO this test
//        checkoutComponent.createOrder
//        checkoutComponent.getOrderId
//        checkoutComponent.getOrderNumber
//        headerComponent.openItemFromUserMenu("Orders");
//        orderComponent.findOrder
//        orderComponent.openOrder
//        actualOrder = orderComponent.getOrder
//        assert(actualOrder,expectedOrder)
    }
}
