package components.salesforce.common;

import components.BasePageComponent;

public class CartPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "CartPage";

    public static boolean isItemInCart(String productName) {
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_CART_BY_NAME", productName));
    }
}
