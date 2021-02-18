package components.salesforce.common;

import components.BasePageComponent;
import components.subcomponents.FilterComponent;
import entities.ProductItem;
import org.openqa.selenium.By;

public class ProductDetailsComponent extends BasePageComponent {

    static String COMPONENT_NAME = "ProductDetailsComponent";
    private static ProductDetailsComponent instance = null;
    public static ProductDetailsComponent  getInstance() {
        if (instance == null)
            instance = new ProductDetailsComponent();

        return instance;
    }


    /**
     * get title from opened item
     * @return String name of item
     */
    public  String getTitle(){
        return getElementText(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_NAME_LABEL"));
    }

    /**
     * add item into cart
     * @return void
     */
    public  void addToCart() {
        addToCart(0);
    }

    //TODO fix it
    public void addToCart(int quantity) {
        reporter.info("Add to Cart");
        if (quantity != 0)
            setQuantity(quantity);

        if (getQuantity() <= 0) {
            reporter.warn("Quantity is incorrect");
            setQuantity(1);
        }
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BUTTON"));
    }



    //TODO get value / now work incorrect
    public int getQuantity(){

        return Integer.parseInt(getElementTextIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"QUANTITY")));
    }



    public void setQuantity(int count){
        reporter.info("Set quantity "+count);
        setText(LOCATORS.getBy(COMPONENT_NAME,"QUANTITY"),String.valueOf(count));
    }

    public ProductItem getProductObject(){
        ProductItem product = new ProductItem();

        //
        // get name
        product.setName(getTitle());



        return product;
    }
}
