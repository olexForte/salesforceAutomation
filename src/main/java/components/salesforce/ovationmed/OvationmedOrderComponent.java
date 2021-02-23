package components.salesforce.ovationmed;

import components.salesforce.common.OrderComponent;
import entities.OrderItem;
import entities.ProductItem;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;

public class OvationmedOrderComponent extends OrderComponent {

    @Override
    public OrderItem getOrderFromPage(){
        OrderItem order = new OrderItem();
        // get main fields
        order.setOrderNumber(getElementText(LOCATORS.getBy(COMPONENT_NAME,"ORDER_NUMBER")));
        order.setSummaryPrice(getElementText(LOCATORS.getBy(COMPONENT_NAME,"SUMMARY_PRICE")));
        order.setDate(getElementText(LOCATORS.getBy(COMPONENT_NAME,"ORDER_DATE")));
        //order.setCount(String.valueOf(findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS")).size())); // tODO think?
        //order.products=getProductsFromPage(order.getCount());

        //get products
        order.setProducts(getListOfProductsFromOrder());

        //get additional fields
        order.addField("Shipping Information", getElementText(LOCATORS.getBy(COMPONENT_NAME, "SHIPPING_INFORMATION_PANEL")));

        return order;
    }

    public List<ProductItem> getListOfProductsFromOrder(){
        List<ProductItem> result = new LinkedList<>();

        //TODO add processing of column index
        for(WebElement row : findElements(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCTS_ROW"))){

            ProductItem item = new ProductItem();

            item.setId(row.findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "2")).getText());
            item.setName(findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "3")).getText());
            item.addField("Status", row.findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "4")).getText());
            item.setCount(row.findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "5")).getText());
            item.setPrice(row.findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "6")).getText());
            item.addField("Tax", row.findElement(LOCATORS.getBy(COMPONENT_NAME,"TABLE_PRODUCT_CELL_BY_INDEX", "8")).getText());

            result.add(item);
        }

        return result;
    }

}
