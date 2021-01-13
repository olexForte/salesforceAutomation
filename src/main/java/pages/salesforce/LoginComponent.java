package pages.salesforce;

import configuration.LocatorsRepository;
import org.openqa.selenium.By;
import pages.BasePageComponent;

import static pages.BasePageComponent.*;

public class LoginComponent extends BasePageComponent {

    static String name = "loginComponent";

    static By inputUsername    = locatorsRepository.getBy(name, "LOGIN_INPUT"); // By.xpath("//input[@id='username']");
    static By inputPassword    = locatorsRepository.getBy(name, "PASSWORD_INPUT");//By.xpath("//input[@id='password']");
    static By buttonLogin      = locatorsRepository.getBy(name, "LOGIN_BUTTON");//By.xpath("//input[@id='Login']");

    public static void loginAs(String... params){ //} username, String password){

        String username = params[0];
        String password = params[1];
        String domain = null;
        if(params.length > 2)
            domain = params[2];
        findElement(inputUsername).sendKeys(username);
        findElement(inputPassword).sendKeys(password);
        if(domain != null)
            findElement(inputPassword).sendKeys(domain);
        findElement(buttonLogin).click();
        waitForPageToLoad();
    }

    public boolean checkSomething(){return true;};
    //
    //div[@class='slds-icon-waffle']
    //input[@type='search'][@placeholder='Search apps and items...']
    //a[contains(.,'Commerce') and @data-label]

    //menu
    //button[@aria-label='Show Navigation Menu']
    //a[@class='menuItem']"

    //setup left
    //*[name()='svg'][@data-key='setup']
    //a[@title='Setup']

    //Setup search
    //"//input[@type='search'][@placeholder='Quick Find']

}
