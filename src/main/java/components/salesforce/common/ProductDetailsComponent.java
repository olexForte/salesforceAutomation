package components.salesforce.common;

import components.BasePageComponent;
import components.subcomponents.FilterComponent;
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
        reporter.info("Add to Cart");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BUTTON"));
    }
}
