package salesforce;

import components.salesforce.common.OrderComponent;
import components.salesforce.common.HeaderComponent;
import components.salesforce.common.OrderSummaryComponent;
import entities.OrderItem;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;
import utils.OrdersComparator;
import utils.TypesOfComparison;

import java.util.HashMap;

public class OrderCompareTest extends BaseUITest{

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private OrderSummaryComponent orderSummaryComponent = OrderSummaryComponent.getInstance();
    private OrderComponent  orderComponent = OrderComponent.getInstance();

    @Test(testName = "Get order and compare")
    public void orderCompareTest() {
        HashMap<String,String> params = dataRepository.getParametersForTest("OrderCompareTest");
        OrderItem expectedOrder = OrderItem.toObject(params.get("EXPECTED_ORDER"));
        OrderItem actualOrder;

        logIn(false);
        headerComponent.openItemFromUserMenu(params.get("MY_ORDERS_MENU_ITEM_NAME"));

        orderSummaryComponent.findOrder(params.get("ORDER_NUMBER"));
        orderSummaryComponent.openOrder(params.get("ORDER_NUMBER"));
        actualOrder = orderComponent.getOrderFromPage();
        TypesOfComparison type = new TypesOfComparison(true,true,true);
        HashMap<String,String> differences = OrdersComparator.compareOrders(actualOrder,expectedOrder,type);

        reporter.processDifference(differences);
        if(differences.size()>0) Assert.fail("Differences found");
    }


}
