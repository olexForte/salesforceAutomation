package components.salesforce;

import components.BasePageComponent;
import components.subcomponents.FilterComponent;

public class MainPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "mainPage";

//TODO
// To do 2 methods second with parameter filter ?? --- " with filter"
    public static void openAddressFromTable(String name,String filter){
        reporter.info("Opening Listing address " + name+" with filter");
        FilterComponent.applyFilter(filter);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_ADDRESS_LINK", name));
        waitForPageToLoad();
    }
}
