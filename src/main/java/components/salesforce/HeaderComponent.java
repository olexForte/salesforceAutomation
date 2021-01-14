package components.salesforce;

import components.BasePageComponent;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "headerComponent";

    public static int getCountItemInCart(){
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "CART_ICON")).replaceAll("[^0-9]", ""));
    }



}
