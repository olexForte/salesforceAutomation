package pages.salesforce;

import org.openqa.selenium.By;
import pages.BasePageComponent;



public class AddressListingComponent extends BasePageComponent {

    static String COMPONENT_NAME = "listingAddressDetailsPAge";

    public static boolean checkTitle(String title){
        sleepFor(5000);
        if(title.equals(
                findElementIgnoreException(locatorsRepository.getBy(COMPONENT_NAME, "TITLE_LABEL"),5000)// timeout 5 sec didn`t work ?? TODO
                        .getText()))
        return true;

        return false;
    }

    public static void openTab(String tab_name) {
        clickOnElement(locatorsRepository.getBy(COMPONENT_NAME,"HEADER_TAB_BY_NAME",tab_name));
    }

    public static void openOrderSummary(String OrderSumaryyId){
        sleepFor(2000);
        clickOnElement(locatorsRepository.getBy(COMPONENT_NAME, "Order_Summary_By_Id",OrderSumaryyId),5000);
        waitForPageToLoad();
    }

    private static void clearDataInput(){
        findElement(locatorsRepository.getBy(COMPONENT_NAME,"Order_Summary_Date"));
    }

    public static void searchByData(String data){
        clearDataInput();
        findElement(locatorsRepository.getBy(COMPONENT_NAME,"Order_Summary_Date")).sendKeys(data);
        clickAnyWhere();
    }
}
