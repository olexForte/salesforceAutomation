package components.salesforce.common;

import components.BasePageComponent;
import configuration.ProjectConfiguration;
import org.openqa.selenium.By;
import utils.BaseUITest;

public class SearchComponent extends BasePageComponent {

//    SearchComponent instance = null;
//    SearchComponent getInstance(){
//        if (instance == null)
//            if (ProjectConfiguration.getConfigProperty("config").contains("asd"))
//                instance = SearchComponent();
//            else
//                instance = new AnotherSearchCompnent();
//            return instance;
//    }

    public static String COMPONENT_NAME = "SearchComponent";
    //TODO ask about memory
    //public static By Search_Field =LOCATORS.getBy(COMPONENT_NAME,"SEARCH_INPUT");
   // public static By Search_Button = LOCATORS.getBy(COMPONENT_NAME, "SEARCH_BUTTON");

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
}
