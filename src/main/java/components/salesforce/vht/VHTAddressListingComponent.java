package components.salesforce.vht;

import components.subcomponents.FilterComponent;
import org.openqa.selenium.By;
import components.BasePageComponent;


public class VHTAddressListingComponent extends BasePageComponent {

    static public String COMPONENT_NAME = "ListingAddressDetailsPage";
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

    public static void clickOnOrderTitle(String orderSummaryId){
        reporter.info("Click on order summary "+orderSummaryId);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ORDER_SUMMARY_LINK_BY_TEXT",orderSummaryId));
        waitForPageToLoad();
    }

    public static void openOrderSummary(String orderNumber, String filter){
        openTab(ORDER_SUMMARY_TAB);
        FilterComponent.applyFilter(filter);
        clickOnOrderTitle(orderNumber);
    }
}
