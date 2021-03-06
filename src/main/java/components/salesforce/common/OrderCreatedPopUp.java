package components.salesforce.common;

import components.BasePageComponent;

public class OrderCreatedPopUp extends BasePageComponent {

    public static String COMPONENT_NAME="OrderCreatedPopUp";
    private static OrderCreatedPopUp instance = null;
    public static OrderCreatedPopUp  getInstance() {
        if (instance == null)
            instance = new OrderCreatedPopUp();

        return instance;
    }
    /**
     * Check if popup is open "after added product to cart"
     * @return boolean
     */
    public boolean isPopupDisplayed(){
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "VIEW_CART_BUTTON"));
    }
    /**
     * click in button "view cart"
     * @return void
     */
    public void  clickOnViewCartButton(){
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "VIEW_CART_BUTTON"));
    }

    public void clickOnContinueShopping() {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "CONTINUE_SHOPPING_BUTTON"));
    }
}
