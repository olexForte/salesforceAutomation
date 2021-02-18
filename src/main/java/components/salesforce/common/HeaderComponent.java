package components.salesforce.common;

import components.BasePageComponent;
import org.openqa.selenium.WebElement;

public class HeaderComponent extends BasePageComponent {

   public static String COMPONENT_NAME = "HeaderComponent";
   private static HeaderComponent instance = null;

    public static HeaderComponent  getInstance() {
        if (instance == null)
            instance = new HeaderComponent();

        return instance;
    }



    /**
     * Get count of item from cart button
     * @return int
     */
    public int getCountItemInCart(){
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "CART_ICON")).replaceAll("[^0-9]", ""));
    }

    /**
     * Wait for number of item in cart
     * @param expectedNumberOfItemsInCart
     * @return void
     */
    public void waitForNumberOfItemsInCart(int expectedNumberOfItemsInCart) {
        waitForElementText(LOCATORS.getBy(COMPONENT_NAME,"CART_ICON"), String.valueOf(expectedNumberOfItemsInCart));
    }



    /**
     * Open item in user profile menu
     * @param itemName String
     * @return void
     */
    public void openItemFromUserMenu(String itemName){
        openUserProfileMenu();
        reporter.info("Open user menu");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_MENU_ITEM_BY_NAME",itemName),SHORT_TIMEOUT);
    }

    /**
     * Open menu user profile
     * @return void
     */
    private void openUserProfileMenu(){
        reporter.info("Open user profile menu");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"USER_PROFILE_MENU"),SHORT_TIMEOUT);
    }
    /**
     * return true if search field exist
     * @return boolean
     */
    public boolean isSearchExist (){
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_INPUT"),SHORT_TIMEOUT)!=null)
        {
            reporter.info("Search exists");
            return true;
        }
        reporter.info("Search doesn`t exist");
        return false;
    }

    /**
     * Find query in search field
     * @param query String
     * @return void
     */
    public void findByQuery(String query){
        reporter.info("Search for: " + query);
        if(isSearchExist())
        {

            setText(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_INPUT"),query,SHORT_TIMEOUT);
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_BUTTON"));
        }
    }

    /**
     * Navigate to some item in header menu
     * @param navigationWay String
     * @return void
     */
    public String navigateByHeaderMenuToItem(String navigationWay){
        reporter.info("Navigate to "+navigationWay);
        String[] steps=navigationWay.split("/");
        for(String step: steps){
            openItemFromHeaderMenu(step);
        }
        return steps[steps.length-1];
    }


    /**
     * click by  item in header menu
     * @param item String
     * @return void
     */
    private void openItemFromHeaderMenu(String item){
        reporter.info("Open item "+item);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"NAVIGATION_ITEM_BY_NAME",item));
    }

    public void openCart() {
        reporter.info("Open cart");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"CART_ICON"));
    }
}
