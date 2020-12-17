package pages.salesforce;

import org.openqa.selenium.By;
import pages.BasePage;
import pages.LocatorsRepository;

import static pages.BasePageComponent.*;
import static pages.LocatorsRepository.get;

public class LoginComponent {

    String name = "loginComponent";
    //LocatorsRepository

    By inputUsername = get(name, "LOGIN_INPUT"); // By.xpath("//input[@id='username']");
    By inputPassword = get(name, "PASSWORD_INPUT");//By.xpath("//input[@id='password']");
    By buttonLogin = get(name, "LOGIN_BUTTON");//By.xpath("//input[@id='Login']");

    public void loginAs(String... params){ //} username, String password){
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
