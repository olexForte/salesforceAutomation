package components.salesforce.vht;

import components.BasePageComponent;

public class VHTOrderSummaryComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderSummaryComponent";

    public static String getTitle() {
        return getElementText(LOCATORS.getBy(COMPONENT_NAME, "ORDER_TITLE_LABEL")).replaceAll("[^0-9a-zA-Z]","");
    }

    public static void startReorder(){
        reporter.info("Start reorder");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"START_REORDER_BUTTON"));
        waitForPageToLoad();
    }


}
