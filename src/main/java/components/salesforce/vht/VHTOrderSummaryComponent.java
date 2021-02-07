package components.salesforce.vht;

import components.BasePageComponent;

public class VHTOrderSummaryComponent extends BasePageComponent {

    static String COMPONENT_NAME = "OrderSummaryComponent";

    private static VHTOrderSummaryComponent instance = null;
    public static VHTOrderSummaryComponent  getInstance() {
        if (instance == null)
            instance = new VHTOrderSummaryComponent();

        return instance;
    }

    public  String getTitle() {
        return getElementText(LOCATORS.getBy(COMPONENT_NAME, "ORDER_TITLE_LABEL")).replaceAll("[^0-9a-zA-Z]","");
    }

    public  void startReorder(){
        reporter.info("Start reorder");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"START_REORDER_BUTTON"));
        waitForPageToLoad();
    }


}
