package components.salesforce.common;

import components.BasePageComponent;

import java.util.List;

public class SearchResultComponent extends BasePageComponent {
    public static String COMPONENT_NAME = "SearchResultComponent";

    public static void openItemFromTable(String productName) {
        reporter.info("Open item from table: "+productName);
        if(isSearchResultExist()){
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT", productName));
        }
    }

    public static void addToCart(String productName){
        reporter.info("Add item to cart: "+productName);
        if(isSearchResultExist())
        {
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "ADD_TO_CART_BY_NAME", productName));
        }
    }

    public static int getTotalNumberOfResults() {
        reporter.info("Get count item from result");
        //take third group of digits in line. Example of line: 1 - 20 of 105 Results for "Thumb Spica, Right, X-Small, Retail"
        return Integer.parseInt(getElementText(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_RESULT_TITLE")).replaceAll("\\d+\\D+\\d+\\D+(\\d+).*", "$1"));
    }

    public static int getNumberOfItemsOnAPage(){
        return findElements(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_CART"),SHORT_TIMEOUT).size();
    }


    public static boolean isSearchResultExist(){
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

    public static boolean isItemInResult(String itemName){
        reporter.info("Find item in result:"+itemName);
        return findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT",itemName)) != null;
    }

    //TODO implement search by pages
    public static boolean areItemsInResult(List<String> items)
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
