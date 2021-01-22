package components.salesforce.common;

import components.BasePageComponent;
import components.subcomponents.FilterComponent;
import org.openqa.selenium.By;

public class ProductDetailsComponent extends BasePageComponent {

    static String COMPONENT_NAME = "ProductDetailsComponent";

    static By TITLE_LABEL = LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_NAME_LABEL");

    public static String getTitle(){
        return getElementText(TITLE_LABEL);
    }

    public static void addToCart() {
        reporter.info("Add to Cart");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BUTTON"));
    }
}
