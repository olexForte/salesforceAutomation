package components.salesforce;

import components.subcomponents.OrderDetailsFilter;
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

    private static void clearDateInput(){
        findElement(LOCATORS.getBy(COMPONENT_NAME,"ORDER_SUMMARY_DATE_INPUT")).clear();
    }

    public static void searchByDate(String date){
        reporter.info("Set date: " + date);
        clearDateInput();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"ORDER_SUMMARY_DATE_INPUT")).sendKeys(date);
        pressTab();
    }

    public static void openOrderSummary(String orderNumber, String filter){
        reporter.info("Open order summary by title "+" with "+filter);
        openTab(ORDER_SUMMARY_TAB);
        applyFilter(filter);
        clickOnOrderTitle(orderNumber);
    }

    private static void applyFilter(String filter) {
        if (filter == null || filter.equals(""))
            return;

        OrderDetailsFilter f = new OrderDetailsFilter(filter);
        f.apply();
    }

    public static void searchByDateRange(String dateFrom, String dataTo) {

    }

    public static void searchByText(String search) {

    }

    public static void searchByStatus(String status) {

    }
}
