package pages.salesforce;

import pages.BasePageComponent;

public class OrderSummaryComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderSummaryComponent";

    public static boolean checkTitle(String title) {
            sleepFor(5000);//TODO
            if(findElementIgnoreException(locatorsRepository.getBy(COMPONENT_NAME, "ORDER_TITLE_LABEL",title)) !=null)
                return true;

            return false;
    }

    public static void startReorder(){

        clickOnElement(locatorsRepository.getBy(COMPONENT_NAME,"START_REORDER_BUTTON"));
        waitForPageToLoad();
    }


}
