package components.salesforce;

import components.BasePageComponent;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "headerComponent";

    public static int getCountItemInCart(){
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "CART_ICON")).replaceAll("[^0-9]", ""));
    }


    //TODO
    // Remove this fear method
    public static boolean ifCountItemInCartEquals(int expected)
    {
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"CART_COUNT_BY_NUMBER",
                Integer.toString(expected)),SHORT_TIMEOUT)!=null)
            return true;

        return false;
    }

    public static void waitForNumberOfItemsInCart(int expectedNumberOfItemsInCart) {
        waitForElementText(LOCATORS.getBy(COMPONENT_NAME,"CART_ICON"), String.valueOf(expectedNumberOfItemsInCart));
    }
}
