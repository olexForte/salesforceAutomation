package components.salesforce.vht;

import components.BasePageComponent;
import components.salesforce.common.MainPageComponent;
import components.subcomponents.FilterComponent;

public class VHTMainPageComponent extends MainPageComponent {

    static String COMPONENT_NAME = "VHTMainPage";


    public  void openAddressFromTable(String name){
    reporter.info("Opening Listing address " + name);
    clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_LINK_BY_NAME", name));
    waitForPageToLoad();
    }

    public  void openAddressFromTable(String name,String filter){
        reporter.info("Opening Listing address " + name+" with filter");
        FilterComponent.applyFilter(filter);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_LINK_BY_NAME", name));
        waitForPageToLoad();
    }

    public  void openProofPhotographsFromTable(String name, String filter){
        reporter.info("Opening proof photographs by Listing address " + name+" with filter");
        FilterComponent.applyFilter(filter);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_PROOF_PHOTOS_LINK", name));
        waitForPageToLoad();
    }

    public  void openProofPhotographsFromTable(String name){
        reporter.info("Opening proof photographs by Listing address "+ name);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "LISTING_ADDRESS_PROOF_PHOTOS_LINK", name));
        waitForPageToLoad();
    }
}
