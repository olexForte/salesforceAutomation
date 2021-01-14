package components.salesforce;

import components.BasePageComponent;

public class OrderCreatedPopUp extends BasePageComponent {

    public static String COMPONENT_NAME="orderCreatedPopUp";

    public static boolean checkIfExist(){
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "CONTINUE_SHOPPING_BUTTON"));
    }

}
