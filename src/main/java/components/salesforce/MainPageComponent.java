package components.salesforce;

import components.BasePageComponent;

public class MainPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "mainPage";

    public static void openAddressFromTable(String name){
        reporter.info("Opening Listing address " + name);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_ADDRESS_LINK", name));
        waitForPageToLoad();
    }

}
