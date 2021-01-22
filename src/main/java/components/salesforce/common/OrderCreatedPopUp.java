package components.salesforce.common;

import components.BasePageComponent;

public class OrderCreatedPopUp extends BasePageComponent {

    public static String COMPONENT_NAME="OrderCreatedPopUp";

    public static boolean isPopupDisplayed(){
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "CONTINUE_SHOPPING_BUTTON"));
    }

    public static void  clickOnViewCartButton(){
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "VIEW_CART_BUTTON"));
    }
}
