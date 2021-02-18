package components.salesforce;

import components.BasePageComponent;
import components.salesforce.common.CartPageComponent;
import entities.OrderItem;

public class OrderComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderComponent";

    private static OrderComponent instance = null;

    public static OrderComponent  getInstance() {
        if (instance == null)
            instance = new OrderComponent();

        return instance;
    }

    public void findOrder(String order_summary_number) {
    }

    public void openOrder(String order_summary_number) {
    }

//    public OrderItem getOrder() {
//    }
}
