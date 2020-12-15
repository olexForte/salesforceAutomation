package pages;

import org.openqa.selenium.By;

public class LoginPage extends BasePage{


    By inputUsername = By.xpath("//input[@id='username']");
    By inputPassword = By.xpath("//input[@id='password']");
    By buttonLogin = By.xpath("//input[@id='Login']");

    public void loginAs(String username, String password){
        findElement(inputUsername).sendKeys(username);
        findElement(inputPassword).sendKeys(password);
        findElement(buttonLogin).click();
        waitForPageToLoad();
    }

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
