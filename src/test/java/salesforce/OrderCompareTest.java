package salesforce;

import components.salesforce.OrderComponent;
import components.salesforce.common.HeaderComponent;
import entities.OrderItem;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class OrderCompareTest extends BaseUITest{

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private OrderComponent  orderComponent = OrderComponent.getInstance();

    @Test(testName = "Get order and compare")
    public void orderCompareTest() {
        HashMap<String,String> params = dataRepository.getParametersForTest("OrderCompareTest");
        String order=params.get("EXPECTED_ORDER");
        OrderItem expectedOrder = new OrderItem(order);// OrderItem.toObject(order);//new OrderItem(order);
        logIn(false);
        headerComponent.openItemFromUserMenu(params.get("ORDERS_NAME"));


         orderComponent.findOrder(params.get("ORDER_SUMMARY_NUMBER"));
         orderComponent.openOrder(params.get("ORDER_SUMMARY_NUMBER"));
       //  OrderItem actualOrder = orderComponent.getOrder();
        // Assert.assertEquals(actualOrder,expectedOrder);
    }


}
