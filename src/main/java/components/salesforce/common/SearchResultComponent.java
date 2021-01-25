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
//TODO this method work incorrect
    public static int getCountOfResult(){
        reporter.info("Get count item from result");
            return findElements(LOCATORS.getBy(COMPONENT_NAME, "ITEM_IN_CART"),SHORT_TIMEOUT).size();
    }


    public static boolean isSearchResultExist(){
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME, "SEARCH_RESULT"),SHORT_TIMEOUT)!=null)
        {
            reporter.info("Search result exist");
            return true;
        }
        else{
            reporter.info("Search result isn`t exist");
        }
        return false;
    }

    public static boolean isItemInResult(String itemName){
        reporter.info("Find item in result:"+itemName);
            if (findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT",itemName))!=null)
            {
                return true;
            }
        return false;
    }

    public static boolean isItemsInResult(List<String> items)
    {
        reporter.info("Find items in result: "+items.toString());
        if(isSearchResultExist())
        {
            for(String item: items)
            {
                if(isItemInResult(item)==false)
                    return false;
            }
            return true;
        }
        return false;
    }

}
