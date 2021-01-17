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
        if(findElement(LOCATORS.getBy(COMPONENT_NAME,"CART_COUNT_BY_NUMBER",
                Integer.toString(expected)))!=null)
            return true;

        return false;
    }

}
