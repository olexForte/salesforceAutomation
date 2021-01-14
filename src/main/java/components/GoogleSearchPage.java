package components;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Created by Admin on 10/20/2017.
 */
public class GoogleSearchPage extends BasePage{

    public GoogleSearchPage(){
        pageURL = "/";
    }

    By searchField = By.name("q");

    public void performSearch(String query){
        reporter.info("Perform search: " + query);
        findElement(searchField).sendKeys(query);
        findElement(searchField).sendKeys(Keys.ENTER);
        waitForPageToLoad();
    }

}
