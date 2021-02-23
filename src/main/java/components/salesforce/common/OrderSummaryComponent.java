package components.salesforce.common;

import components.BasePageComponent;
import components.salesforce.ovationmed.OvationmedOrderComponent;
import configuration.ProjectConfiguration;
import entities.OrderItem;

import java.util.List;

public class OrderSummaryComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderSummaryComponent";

    private static OrderSummaryComponent instance = null;

    public static OrderSummaryComponent getInstance() {
        if (instance == null)
                instance = new OrderSummaryComponent();
        return instance;
    }

    public void findOrder(String order_summary_number) {
        setText(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_FILED"),order_summary_number);
        pressTab();

    }

    public void openOrder(String order_summary_number) {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"ORDER_BY_NUMBER",order_summary_number));
    }

    public List<OrderItem> getOrdersFromPage(){
//        OrderItem order = new OrderItem();
//        order.setSummaryPrice(getElementText(LOCATORS.getBy(COMPONENT_NAME,"SUMMARY_PRICE")));
//        order.setDate(getElementText(LOCATORS.getBy(COMPONENT_NAME,"ORDER_DATE")));
//        order.setCount(String.valueOf(findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS")).size()));
        //order.products=getProductsFromPage(order.getCount());
        return null;
    }
}
