package components.salesforce.common;

import components.BasePageComponent;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "HeaderComponent";
   public static SearchComponent search;


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
        reporter.info("Open user profile");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_MENU_ITEM_BY_NAME",itemName),SHORT_TIMEOUT);
    }


    private static void openUserProfileMenu(){
        reporter.info("Open user profile menu");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_PROFILE_MENU"),SHORT_TIMEOUT);
    }


}
