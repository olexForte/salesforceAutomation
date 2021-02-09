package components;

import configuration.LocatorsRepository;
import configuration.ProjectConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reporting.ReporterManager;
//import web.Selenium4Wrapper;

import java.util.ArrayList;

import java.util.List;

/**
 * Represents active component of Web page
 */
public class BasePageComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePageComponent.class);

    public final static ReporterManager reporter = ReporterManager.Instance;

    public final static LocatorsRepository LOCATORS = LocatorsRepository.Instance;

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static final int DEFAULT_TIMEOUT = getTimeout();
    public static final int SHORT_TIMEOUT = getShortTimeout();
    public static final int STATIC_TIMEOUT =  getStaticTimeout();

    private static int getTimeout() {
        String timeout = ProjectConfiguration.getConfigProperty("DefaultTimeoutInSeconds");
        if (timeout == null ) {
            reporter.warn("DefaultTimeoutInSeconds parameter was not found");
            timeout = "15";
        };

        return Integer.parseInt(timeout);
    }

    private static int getShortTimeout() {
        String timeout = ProjectConfiguration.getConfigProperty("ShortTimeoutInSeconds");
        if (timeout == null ) {
            timeout = "3";
        };

        return Integer.parseInt(timeout);
    }

    private static int getStaticTimeout() {
        String timeout = ProjectConfiguration.getConfigProperty("StaticTimeoutMilliseconds");
            if (timeout == null ) {
                    timeout = "1000";
            };
        return Integer.parseInt(timeout);
    }

    /**
     * Get driver from current thread
     * @return
     */
    public static WebDriver driver(){
        return driver.get();
    }

//    //TODO
//    public void slow(){
//        ((Selenium4Wrapper) (driver.get())).slowDown();
//    }
    /**
     * Reload page
     */
    public static void reloadPage() {
        driver().navigate().refresh();
    }

    /**
     * open URL
     * @param url
     */
    public static void open(String url) {

        reporter.info("Opening the page: " + "\"" + url + "\"");
        driver().get(url);
        driver().manage().window().maximize();
    }

    /**
     * Close Driver
     */
    public static void close() {
        reporter.info("Closing the browser");
        driver().close();
    }

    /**
     * Get current URL
     * @return URL
     */
    public static String getCurrentURL() {
        try {
            return driver().getCurrentUrl();
        } catch (Exception e) {
            return "Webdriver failed to get URL";
        }
    }

    /**
     * Set text to element
     * @param element
     * @param value
     */
    //TODO clear don`t work // need to set value=""
    public static void setText(By element, String value,int... timeout){
        findElement(element,timeout).click();
        findElement(element,timeout).clear();
        if (value != null) {
            findElement(element).sendKeys(value);
        }
    }

    /**
     * Check that text is present in Page source
     * @param text
     * @return
     */
    public static boolean isTextPresent(String text) {
        reporter.info("Validate text present: " + text);
        return driver().getPageSource().contains(text);
    }

    /**
     * Get text of element with sleep=timeout if element don`t find
     * @param by
     * @param timeout
     * @return
     */
    public static String getElementText(By by, int... timeout) {
        String result = "";
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        for (int attemptNumber = 0; attemptNumber < timeoutForFindElement; attemptNumber++) {
            try {
                WebElement elem = findElement(by, 1);
                if (elem == null)
                    continue;
                result = elem.getText();
                //get value if text = ""
                if (result.equals("") && (elem.getAttribute("value") != null && !elem.getAttribute("value").equals("")))
                    result = elem.getAttribute("value"); //TODO add type validation
            }catch (Exception e){
                LOGGER.warn("getElementText " + by.toString() + " " +  e.getMessage());
                result = "";
            }
            if (!result.equals(""))
                break;
            sleepFor(1000);
        }
        return result;
    }


    /**
     * Get text of element with sleep=timeout if element don`t find
     * @param by
     * @param timeout
     * @return
     */
    public static String getElementTextIgnoreException(By by, int... timeout) {
        String result = null;
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];

        try {
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.presenceOfElementLocated(by));
            WebElement elem = findElement(by, 1);
            if (elem != null) {
                result = elem.getText();
            }
        } catch (Exception e) {
            LOGGER.warn("getElementText " + by.toString() + " " + e.getMessage());
        }
        return result;
    }

    /**
     * Press TAB using Actions
     */
    public static void pressTab() {
        Actions a = new Actions(BasePageComponent.driver()); a.sendKeys(Keys.TAB).build().perform();
    }

    /**
     * Check if element is displayed
     * @param by
     * @return
     */
    public static boolean isElementDisplayed(By by,int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        for (int attemptNumber = 0; attemptNumber < timeoutForFindElement; attemptNumber++) {
            try {
                if(driver().findElement(by).isDisplayed()==true);
                    return true;
            } catch (Exception e) {
                if(attemptNumber>=timeoutForFindElement)
                {
                    reporter.fail("isElementDisplayed Error",  e);
                    throw e;
                }
            }
            sleepFor(1000);
        }
        return false;
    }

    /**
     * Check if element is displayed by string
     * @param by
     * @return
     */
    public static boolean isElementDisplayed(String by,int... timeout) {
    return isElementDisplayed(By.xpath(by),timeout);
    }

    public static void selectFromDropdown(By element, String value){
        Select dropdown = new Select(findElement(element));
        dropdown.selectByVisibleText(value);
    }

    /**
     * Click on Element ignore Exception with timeout
     * @param element By
     * @param timeout int
     * @return
     */
    public static void clickOnElementIgnoreException(By element, int... timeout) {
        waitForPageToLoad();
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        try {
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.visibilityOfElementLocated(element));
            driver().findElement(element).click();
        } catch (Exception e) {
            // nothing
        }
        waitForPageToLoad();
    }

    /**
     * Find Element ignore Exception with timeout
     * @param element By
     * @param timeout int
     * @return WebElement
     */
    public static WebElement findElementIgnoreException(By element, int... timeout) {
        waitForPageToLoad();
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.visibilityOfElementLocated(element));
            return driver().findElement(element);

        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Find Element ignore Exception with timeout
     * @param element By
     * @param timeout int
     * @return List<WebElement> list of element
     */
    public static List<WebElement> findElementsIgnoreException(By element, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.presenceOfElementLocated(element));
            return driver().findElements(element);
        } catch (Exception e) {
            //reporter.info("Got exception. Exception is expected and ignored.");
            return new ArrayList<WebElement>();
        }
    }
    /**
     * Click on Element ignore Exception with sleep=timeout if element don`t find
     * @param element By
     * @param timeout int
     * @return
     */
    public static void clickOnElement(By element, int... timeout) {
        //error message: javascript error: Cannot read property 'defaultView' of undefined
        String errorMessage = "javascript error: Cannot read property 'defaultView' of undefined";
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        for (int attemptNumber = 0; attemptNumber < timeoutForFindElement; attemptNumber++) {
            try {
                findElement(element, 1).click();
                LOGGER.info("Clicked");
                break;
            } catch (JavascriptException e){
                LOGGER.warn("Failure clicking on element " + element.toString() + " " + e.getMessage());
                if(e.getMessage().contains("Cannot read property 'defaultView' of undefined")) { // processing of known issues with lightning elements
                    JavascriptExecutor executor = (JavascriptExecutor) driver();
                    executor.executeScript("arguments[0].click();", findElement(element, 1));
                    LOGGER.info("Clicked using JS");
                    break;
                }
            } catch (Exception e) {
                if (e.getMessage().contains(errorMessage))
                {
                    clickOnElementWithJSIgnoreException(element,timeout);
                    LOGGER.info("Clicked by JS");
                    break;
                }

                LOGGER.warn("Failure clicking on element " + element.toString() + " " + e.getMessage());
                if (attemptNumber >= timeoutForFindElement) {
                    reporter.fail("Failure clicking on element",  e);
                    throw e;
                }
                sleepFor(1000);
            }
        } 
        waitForPageToLoad();
    }
    /**
     * Click on Element ignore Exception with using JS
     * @param by By
     * @param timeout int
     * @return
     */
    private static void clickOnElementWithJSIgnoreException(By by, int... timeout) {
        try {
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click();",findElement(by, timeout));
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Find Element
     * @param element By
     * @param timeout int
     * @return WebElement
     */
    public static WebElement findElement(By element, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.presenceOfElementLocated(element));
            return driver().findElement(element);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * wait for text on element
     * @param locator By
     * @param timeout int
     * @return WebElement
     */
    public static boolean waitForElementText(By locator, String expectedText, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            WebElement element = findElement(locator, timeout);
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.textToBePresentInElement(element, expectedText));
            if (getElementText(locator, 1).contains(expectedText))
                return true;
            else
                return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Find Element by text Xpath
     * @param byString text of Xpath
     * @param timeout int
     * @return WebElement
     */
    public static WebElement findElement(String byString, int... timeout){
        return findElement(By.xpath(byString) ,timeout);
    }

    /**
     * Find Elements
     * @param element By
     * @param timeout int
     * @return List<WebElement> list of web element
     */
    public static List<WebElement> findElements(By element, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.presenceOfElementLocated(element));
            return driver().findElements(element);
        } catch (Exception e) {
            reporter.fail("Failure finding element",  e);
            throw new RuntimeException("Failure finding elements");
        }
    }

    /**
     * Get attribute from element
     * @param element By
     * @param timeout int
     * @param attribute String
     * @return String or Exception
     */
    public static String getAttribute(By element,String attribute, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.visibilityOfElementLocated(element));
            String value = findElement(element).getAttribute(attribute);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failure getting attribute "+attribute+" of an element");
        }
    }

    /**
     * Get attribute from element ignore exception
     * @param element By
     * @param timeout int
     * @param attribute String
     * @return String
     */
    public static String getAttributeIgnoreException(By element,String attribute, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.visibilityOfElementLocated(element));
            String value = findElement(element).getAttribute(attribute);
            return value;
        } catch (Exception e) {
            reporter.info("Got exception. Exception is expected and ignored.");
        }
        return null;
    }

    /**
     * Scroll to Element
     * @param element WebElement
     * @return void
     */
    public static void scrollToElement(WebElement element) {
        waitForPageToLoad();
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView();", element);
    }
    /**
     * Scroll to Element
     * @param element WebElement
     * @return void
     */
    public static void scrollToShopElement(WebElement element){
        waitForPageToLoad();
        ((JavascriptExecutor) driver()).executeScript("arguments[0].focus(); window.scroll(0, window.scrollY+=200)",element);
    }

    /**
     * wait for a page will be load
     * @return void
     */
    public static void waitForPageToLoad(){
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver)
            {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .equals("complete");
            }

        };

        Wait<WebDriver> wait = new WebDriverWait(driver(), DEFAULT_TIMEOUT);

        try
        {
            wait.until(expectation);
        } catch (Exception error)
        {
            reporter.fail("JavaScript readyState query timeout - The page has not finished loading");
        }

//        String source = driver().getPageSource();
//
//        expectation = new ExpectedCondition<Boolean>() {
//
//            public Boolean apply(WebDriver driver)
//            {
//                return ((JavascriptExecutor) driver).executeScript("return jQuery.active")
//                        .equals("0");
//            }
//
//        };
//
//        wait = new WebDriverWait(driver(), DEFAULT_TIMEOUT);
//
//        try
//        {
//            wait.until(expectation);
//        } catch (Exception error)
//        {
//            reporter.fail("The page has not finished loading");
//        }

    }

    /**
     * wait for element
     * @param by By
     * @return void
     */
    static void waitForElement(By by){
        WebDriverWait wait = new WebDriverWait(driver(), DEFAULT_TIMEOUT);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * sleep
     * @param timeout int
     * @return void
     */
    public static void sleepFor(int timeout){
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }

    /**
     * wait for alert will be displayed
     * @param timeout int
     * @return void
     */
    static void waitForAlert(WebDriver driver, int timeout) {
        int i = 0;
        while (i++ < timeout) {
            try {
                Alert alert = driver.switchTo().alert();
                break;
            } catch (NoAlertPresentException e)  // wait for second
            {
                sleepFor(1);
                continue;
            }
        }
    }

    /*
    static void waitForElement(WebDriver driver, By by, int timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    } */


    // Does not work because of geckodriver bug - https://stackoverflow.com/questions/40360223/webdriverexception-moveto-did-not-match-a-known-command
    /**
     * hower to item
     * @param element By
     * @return void
     */
    public static void hoverItem(By element){
        sleepFor(2000);
        reporter.info("Put mouse pointer over element: " + element.toString());
        Actions action = new Actions(driver());
        action.moveToElement(findElement(element)).build().perform();
    }

    /**
     * Swith to some frame
     * @param xpath By
     * @return void
     */
    public void switchToFrame(By xpath) {
        reporter.info("Switch to frame: " + xpath.toString());
        driver().switchTo().frame(findElement(xpath));
    }

    /**
     * Set Driver Context to default content (first or default frame)
     * @return void
     */
    public void switchToDefaultContent(){
        reporter.info("Switch to default content");
        driver().switchTo().defaultContent();
    }

    /**
     * Set Driver Context to default content (first or default frame)
     * @param driver WebDriver
     * @return void
     */
    public static void setDriverContextToPage(WebDriver driver) {
        reporter.info("Setting the context mode to Page");
        driver.switchTo().defaultContent();
    }

    /**
     * Get link from button that opening page in new tab
     * @param by By
     * @param timeout int
     * @return String link new page
     */
    public static String getLinkByClickFromNewTab(By by, int... timeout) {
        try {
            String link;
            String oldTab = driver().getWindowHandle();
            ArrayList<String> tabs1 = new ArrayList<String>(driver().getWindowHandles());
            clickOnElementIgnoreException(by);
            waitForPageToLoad();
            ArrayList<String> tabs2 = new ArrayList<String>(driver().getWindowHandles());

            if (tabs1.size() == tabs2.size() || tabs2.size() > tabs1.size() + 1)
                throw new Exception("link don`t open in new tab, or count of link from this button more than one");

            for (int i = 0; i < tabs2.size(); i++) {
                for (int j = 0; j < tabs1.size(); j++) {
                    if (tabs2.get(i).equals(tabs1.get(j)))
                        tabs2.remove(i);
                }
            }

            driver().switchTo().window(tabs2.get(0)).getCurrentUrl();
            link = driver().getCurrentUrl();
            driver().close();
            driver().switchTo().window(oldTab);
            return link;

        } catch (Exception e) {
            reporter.fail("Failure getting link from button ", e);
            throw new RuntimeException("Failure getting link from button ");
        }
    }
    /**
     * Get link from button that opening page in this tab
     * (can be problem, when button go to page that to do redirect)
     * @param by By
     * @param timeout int
     * @return String link new page
     */
    public static String getLinkByClickFromElement(By by, int... timeout) {
        try {
            String currentPage= driver().getCurrentUrl();
            clickOnElement(by);
            waitForPageToLoad();
            String url =driver().getCurrentUrl();
            driver().navigate().to(currentPage);
            return url;
        } catch (Exception e) {
            reporter.fail("Failure getting link from button ", e);
            throw new RuntimeException("Failure getting link from button ");
        }
    }


}
