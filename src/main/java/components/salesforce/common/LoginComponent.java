package components.salesforce.common;

import configuration.ProjectConfiguration;
import org.openqa.selenium.By;
import components.BasePageComponent;

public class LoginComponent extends BasePageComponent {

    static String COMPONENT_NAME = "LoginComponent";

    static By inputUsername    = LOCATORS.getBy(COMPONENT_NAME, "LOGIN_INPUT"); // By.xpath("//input[@id='username']");
    static By inputPassword    = LOCATORS.getBy(COMPONENT_NAME, "PASSWORD_INPUT");//By.xpath("//input[@id='password']");
    static By buttonLogin      = LOCATORS.getBy(COMPONENT_NAME, "LOGIN_BUTTON");//By.xpath("//input[@id='Login']");

    private static LoginComponent instance = null;
    public static LoginComponent  getInstance() {
        if (instance == null)
            instance = new LoginComponent();

        return instance;
    }

    /**
     * log in store
     * @param params String login ,password
     * @return void
     */
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




    public void clickButtonRegistration(){
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "REGISTRATION_BUTTON"));
    }


    public void OpenLoginPage(){
        open(ProjectConfiguration.getConfigProperty("ClientEnvironmentURL"));
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
