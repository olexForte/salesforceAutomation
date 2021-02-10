package components.salesforce.common;

import components.BasePageComponent;

import java.util.List;

public class SearchResultComponent extends BasePageComponent {
    public static String COMPONENT_NAME = "SearchResultComponent";
    private static SearchResultComponent instance = null;
    public static SearchResultComponent  getInstance() {
        if (instance == null)
            instance = new SearchResultComponent();

        return instance;
    }

    /**
     * open item in table of result
     * @param productName name of product
     * @return void
     */
    public void openItemFromTable(String productName) {
        reporter.info("Open item from table: "+productName);
        if(isSearchResultExist()){
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT", productName));
        }
    }
    /**
     * add item to cart
     * @param productName name of product
     * @return void
     */
    public void addItemToCart(String productName) {
        reporter.info("Add item to cart : "+productName);
        if (isSearchResultExist()){
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BY_NAME", productName));
        }
    }


    /**
     * Add item into cart
     * @param productName name of product
     * @return void
     */
    public void addToCart(String productName){
        reporter.info("Add item to cart: "+productName);
        if(isSearchResultExist())
        {
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BY_NAME", productName));
        }
    }
    /**
     * Get count of result
     * @return int count of result
     */
    //TODO add possibility check if result 1
    public int getTotalNumberOfResults() {
        reporter.info("Get count item from result");
        //take third group of digits in line. Example of line: 1 - 20 of 105 Results for "Thumb Spica, Right, X-Small, Retail"
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_RESULT_TITLE")).replaceAll("\\d+\\D+\\d+\\D+(\\d+).*", "$1"));
    }

    /**
     * Get count of item in the page
     * @return int count of result
     */
    public int getNumberOfItemsOnAPage(){
        return findElements(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_TABLE"),SHORT_TIMEOUT).size();
    }

    /**
     * Check if result page is exist
     * @return boolean
     */
    public boolean isSearchResultExist(){
        if(isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_RESULT")))
        {
            reporter.info("Search result exist");
            return true;
        }
        else{
            reporter.info("Search result doesn`t exist");
        }
        return false;
    }

    /**
     * Check if item is in the page
     * @return int count of result
     */
    public boolean isItemInResult(String itemName){
        reporter.info("Find item in result:"+itemName);
        return (LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT",itemName)) != null;
    }

    //TODO implement search by pages
    public boolean areItemsInResult(List<String> items)
    {
        reporter.info("Find items in result: "+items.toString());
        if(isSearchResultExist())
        {
            boolean result = true;
            for(String item: items)
            {
                if(isItemInResult(item)) {
                    reporter.info("Item was found; " + item);
                } else {
                    reporter.fail("Item was not found; " + item);
                    result = false;
                }
            }
            return result;
        }
        return false;
    }

}
