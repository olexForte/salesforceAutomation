package components.salesforce;

import components.BasePageComponent;
import components.salesforce.common.CartPageComponent;
import entities.OrderItem;
import entities.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class OrderComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderComponent";

    private static OrderComponent instance = null;

    public static OrderComponent  getInstance() {
        if (instance == null)
            instance = new OrderComponent();

        return instance;
    }

    public void findOrder(String order_summary_number) {
        setText(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_FILED"),order_summary_number);
        pressTab();

    }

    public void openOrder(String order_summary_number) {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"ORDER_BY_NUMBER",order_summary_number));
    }

    public OrderItem getOrderFromPage(){
        OrderItem order = new OrderItem();
        order.setSummaryPrice(getElementText(LOCATORS.getBy(COMPONENT_NAME,"SUMMARY_PRICE")));
        order.setDate(getElementText(LOCATORS.getBy(COMPONENT_NAME,"ORDER_DATE")));
        order.setCount(String.valueOf(findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS")).size()));
        //order.products=getProductsFromPage(order.getCount());
        return order;
    }

//    public List<ProductItem> getProductsFromPage(String countString){
//        int count = Integer.valueOf(countString);
//        List<ProductItem> listOfProduct = new ArrayList<>();
//        for(int i =0;i<count;i++){
//            findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS")).get(i);
//        }
//
//    }



//    public OrderItem getOrder() {
//    }
}
