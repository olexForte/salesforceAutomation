package pages.salesforce;

import pages.BasePageComponent;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "headerComponent";

    public static int getCountItemInCart(){

        return Integer.parseInt(findElement(locatorsRepository.getBy(COMPONENT_NAME, "Cart_Icon")).getText().replaceAll("[^0-9]", ""));

    }



}
