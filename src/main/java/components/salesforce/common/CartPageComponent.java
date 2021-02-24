package components.salesforce.common;

import components.BasePageComponent;
import components.salesforce.exGuard.ContactSupportComponent;
import entities.ProductItem;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class CartPageComponent extends BasePageComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartPageComponent.class);

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
    public double getFinalPrice(){
        sleepFor(1);//here
        return Double.valueOf(getElementText(LOCATORS.getBy(COMPONENT_NAME, "FINAL_PRICE")).replaceAll("[^0-9.]",""));
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

    public boolean isItemInCart(ProductItem item) {
        //item.getPrice();
        return false;
    }

    public ProductItem getProductFromCart(int i) {
        return getProductsFromCart().get(i);
    }

    public void proceedToCheckout() {
        reporter.info("Click by button 'Proceed To Checkout'");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"PROCEED_TO_CHECKOUT"));

    }

    public void clearCart() {
        try {
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "CLEAR_CART_BUTTON"));
        } catch (Exception e){
            LOGGER.warn("No Clear Cart button");
        }
    }

    public List<ProductItem> getProductsFromCart() {
        List<ProductItem> result = new LinkedList<>();
        for(WebElement row : findElements(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_CART_TABLE_ROW"))){
            ProductItem item = new ProductItem();
            item.setName(row.findElement(LOCATORS.getBy(COMPONENT_NAME, "ITEM_TITLE_LABEL")).getText());
            item.setCount(row.findElement(LOCATORS.getBy(COMPONENT_NAME, "ITEM_QUANTITY_INPUT")).getAttribute("value"));
            item.setPrice(row.findElement(LOCATORS.getBy(COMPONENT_NAME, "ITEM_PRICE_PER_UNIT_LABEL")).getText());
            result.add(item);
        }
        return result;
    }
}
