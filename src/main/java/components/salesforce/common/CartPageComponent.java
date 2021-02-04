package components.salesforce.common;

import components.BasePageComponent;

public class CartPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "CartPage";

    private static CartPageComponent instance = null;

    public static CartPageComponent  getInstance() {
        if (instance == null)
            instance = new CartPageComponent();

        return instance;
    }


    /**
     * if item is in cart
     * @param productName name of product
     * @return boolean
     */
    public boolean isItemInCart(String productName) {
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_CART_BY_NAME", productName));
    }
}
