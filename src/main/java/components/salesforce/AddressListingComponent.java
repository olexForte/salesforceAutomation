package components.salesforce;

import components.subcomponents.FilterComponent;
import org.openqa.selenium.By;
import components.BasePageComponent;


public class AddressListingComponent extends BasePageComponent {

    static public String COMPONENT_NAME = "listingAddressDetailsPage";
    static By TITLE_LABEL = LOCATORS.getBy(COMPONENT_NAME, "TITLE_LABEL");

    //tab titles
    private static String ORDER_SUMMARY_TAB = "Order Details";

    public static String getTitle(){
        return getElementText(TITLE_LABEL);
    }

    public static void openTab(String tab_name) {
        reporter.info("Open tab "+tab_name);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"HEADER_TAB_BY_NAME",tab_name));
    }

    public static void clickOnOrderTitle(String orderSumaryId){
        reporter.info("Click on order summary "+orderSumaryId);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ORDER_SUMMARY_LINK_BY_TEXT",orderSumaryId));
        waitForPageToLoad();
    }

    public static void openOrderSummary(String orderNumber, String filter){
        openTab(ORDER_SUMMARY_TAB);
        FilterComponent.applyFilter(filter);
        clickOnOrderTitle(orderNumber);
    }
}
