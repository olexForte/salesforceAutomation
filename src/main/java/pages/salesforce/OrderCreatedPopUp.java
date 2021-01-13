package pages.salesforce;

import pages.BasePageComponent;

public class OrderCreatedPopUp extends BasePageComponent {

    public static String COMPONENT_NAME="orderCreatedPopUp";

    public static boolean cleckIfExist(){

        if(findElementIgnoreException(
                locatorsRepository.getBy(COMPONENT_NAME, "CONTINUE_SHOPPING_BUTTON"))!=null)
        return true;

        return false;
    }

}
