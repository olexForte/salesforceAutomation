package salesforce;

import components.salesforce.OrderComponent;
import components.salesforce.common.HeaderComponent;
import entities.OrderItem;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;
import utils.OrdersComparator;
import utils.TypesOfComparison;

import java.util.HashMap;

public class OrderCompareTest extends BaseUITest{

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private OrderComponent  orderComponent = OrderComponent.getInstance();

    @Test(testName = "Get order and compare")
    public void orderCompareTest() {
        HashMap<String,String> params = dataRepository.getParametersForTest("OrderCompareTest");
        OrderItem expectedOrder = OrderItem.toObject(params.get("EXPECTED_ORDER"));
        OrderItem actualOrder = new OrderItem();
        logIn(false);
        headerComponent.openItemFromUserMenu(params.get("ORDERS_NAME"));


         orderComponent.findOrder(params.get("ORDERS_NUMBER"));
         orderComponent.openOrder(params.get("ORDERS_NUMBER"));
         actualOrder= orderComponent.getOrderFromPage();
        TypesOfComparison type = new TypesOfComparison(true,true,false);
        HashMap<String,String> difference= OrdersComparator.compareOrders(actualOrder,expectedOrder,type);
    }


}
