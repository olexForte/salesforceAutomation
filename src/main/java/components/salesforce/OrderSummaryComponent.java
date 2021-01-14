package components.salesforce;

import components.BasePageComponent;

public class OrderSummaryComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderSummaryComponent";

    public static String getTitle() {
        return getElementText(LOCATORS.getBy(COMPONENT_NAME, "ORDER_TITLE_LABEL")).replaceAll("[^0-9a-zA-Z]","");
    }

    public static void startReorder(){

        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"START_REORDER_BUTTON"));
        waitForPageToLoad();
    }


}
