package pages.salesforce;

import org.openqa.selenium.By;
import pages.BasePageComponent;

public class MainPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "mainPage";

    public static void openAddressFromTable(String name){
        clickOnElement(locatorsRepository.getBy(COMPONENT_NAME, "LISTING_ADDRESS_ADDRESS_LINK", name));
        waitForPageToLoad();
    }

}
