package components.salesforce.common;

import components.BasePageComponent;
import org.openqa.selenium.WebElement;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "HeaderComponent";

    public static int getCountItemInCart(){
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "CART_ICON")).replaceAll("[^0-9]", ""));
    }

    public static void waitForNumberOfItemsInCart(int expectedNumberOfItemsInCart) {
        waitForElementText(LOCATORS.getBy(COMPONENT_NAME,"CART_ICON"), String.valueOf(expectedNumberOfItemsInCart));
    }

    public static void searchForProduct(String productName) {
        reporter.info("Search for: " + productName);
        setText(LOCATORS.getBy(COMPONENT_NAME,"GLOBAL_SEARCH_INPUT"), productName);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "GLOBAL_SEARCH_BUTTON"));
    }

    public static void openItemFromUserMenu(String itemName){
        openUserProfileMenu();
        reporter.info("Open user menu");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_MENU_ITEM_BY_NAME",itemName),SHORT_TIMEOUT);
    }


    private static void openUserProfileMenu(){
        reporter.info("Open user profile menu");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_PROFILE_MENU"),SHORT_TIMEOUT);
    }
    public static boolean isSearchExist (){
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_INPUT"),SHORT_TIMEOUT)!=null)
        {
            reporter.info("Search exists");
            return true;
        }
        reporter.info("Search doesn`t exist");
        return false;
    }

    public static void findByQuery(String query){
        reporter.info("Search for: " + query);
        if(isSearchExist())
        {
            setText(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_INPUT"),query,SHORT_TIMEOUT);
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_BUTTON"));
        }
    }


    public static String navigateByHeaderMenuToItem(String navigationWay){
        reporter.info("Navigate to "+navigationWay);
        String[] steps=navigationWay.split("\\|");
        for(String step: steps){
            openItemFromHeaderMenu(step);
        }
        return steps[steps.length-1];
    }

    private static void openItemFromHeaderMenu(String item){
        reporter.info("Open item "+item);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"NAVIGATION_ITEM_BY_NAME",item));
    }

    public static String getNameResultPage() {
        reporter.info("Get name of result page ");
        return getElementText(LOCATORS.getBy(COMPONENT_NAME,"NAME_RESULT_PAGE"));
    }

}
