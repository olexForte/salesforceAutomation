package components.salesforce.common;

import components.BasePageComponent;
import components.salesforce.ovationmed.OvationmedOrderComponent;
import configuration.ProjectConfiguration;
import entities.OrderItem;

public class OrderComponent extends BasePageComponent {

    static public String COMPONENT_NAME = "OrderComponent";

    private static OrderComponent instance = null;

    public static OrderComponent  getInstance() {
        if (instance == null)
            switch (ProjectConfiguration.getConfigProperty("Client")){
                case "ovationmed" :
                    instance = new OvationmedOrderComponent(); break;
                default:
                    instance = new OrderComponent();
            }

        return instance;
    }

    public OrderItem getOrderFromPage(){
        OrderItem order = new OrderItem();
        order.setSummaryPrice(getElementText(LOCATORS.getBy(COMPONENT_NAME,"SUMMARY_PRICE")));
        order.setDate(getElementText(LOCATORS.getBy(COMPONENT_NAME,"ORDER_DATE")));
        //order.setCount(String.valueOf(findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS")).size()));
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
