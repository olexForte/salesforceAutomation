package components.salesforce.common;

import components.BasePageComponent;

public class CartPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "CartPageComponent";

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

    /**
     * Get summary price
     * @return void
     */
    public String getFinalPrice(){
        return getElementTextIgnoreException(LOCATORS.getBy(COMPONENT_NAME, "FINAL_PRICE")).replaceAll("[^0-9.]","");
    }

    /**
     * Remove item from cart
     * @param product_name String
     * @return void
     */
    public void removeItem(String product_name) {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "REMOVE_ITEM_BY_NAME",product_name));
    }

    /**
     * Wait for price
     * @param expectedPrice double
     * @return void
     */
    public void waitForFinalPrice(double expectedPrice) {
        waitForElementText(LOCATORS.getBy(COMPONENT_NAME,"FINAL_PRICE"), String.valueOf(expectedPrice),SHORT_TIMEOUT);
    }
}
