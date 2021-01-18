package components;

import configuration.LocatorsRepository;
import configuration.ProjectConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reporting.ReporterManager;

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
    public static void setText(By element, String value){
        findElement(element).clear();
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
    public static boolean isElementDisplayed(By by) {
        try {
            WebElement element = findElement(by);
            if (element == null)
                return true;
            return element.isDisplayed();
        } catch (Exception e) {
            LOGGER.warn("isElementDisplayed Error", e);
            return false;
        }
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
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        for (int attemptNumber = 0; attemptNumber < timeoutForFindElement; attemptNumber++) {
            try {
                findElement(element, 1).click();
                break;
            } catch (Exception e) {
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

    public static String getAttributeIDIgnoreExecption(By element, int... timeout) {
        waitForPageToLoad();
        try {
            return getAttributeID(element, timeout[0]);
        } catch (RuntimeException e) {
            reporter.info("Got exception. Exception is expected and ignored.");
        }
        return null;
    }

    public static String getAttributeID(By element, int... timeout) {
        int timeoutForFindElement = timeout.length < 1 ? DEFAULT_TIMEOUT : timeout[0];
        waitForPageToLoad();
        try {
            //synchronize();
            (new WebDriverWait(driver(), timeoutForFindElement))
                    .until(ExpectedConditions.visibilityOfElementLocated(element));
            String id = findElement(element).getAttribute("id");
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failure getting attribute id of an element");
        }
    }

    public static void setDriverContextToPage(WebDriver driver) {
        reporter.info("Setting the context mode to Page");
        driver.switchTo().defaultContent();
    }

    public static void scrollToElement(WebElement element) {
        waitForPageToLoad();
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView();", element);
    }

    public static void scrollToShopElement(WebElement element){
        waitForPageToLoad();
        ((JavascriptExecutor) driver()).executeScript("arguments[0].focus(); window.scroll(0, window.scrollY+=200)",element);
    }

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

    static void waitForElement(By by){
        WebDriverWait wait = new WebDriverWait(driver(), DEFAULT_TIMEOUT);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void sleepFor(int timeout){
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }

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
    public static void hoverItem(By element){
        reporter.info("Put mouse pointer over element: " + element.toString());
        Actions action = new Actions(driver());
        action.moveToElement(findElement(element)).build().perform();
    }

    public void switchToFrame(By xpath) {
        reporter.info("Switch to frame: " + xpath.toString());
        driver().switchTo().frame(findElement(xpath));
    }

    public void switchToDefaultContent(){
        reporter.info("Switch to default content");
        driver().switchTo().defaultContent();
    }
    //TODO
        //custom method from Yura
        public static String getLinkFromNewTab(By by, int... timeout){
            try {
                String link;
                String oldTab= driver().getWindowHandle();
                ArrayList<String> tabs1 = new ArrayList<String> (driver().getWindowHandles());
                clickOnElementIgnoreException(by);
                ArrayList<String> tabs2 = new ArrayList<String> (driver().getWindowHandles());

                if(tabs1.size()==tabs2.size()||tabs2.size()>tabs1.size()+1)
                        throw new Exception("link didn`t open in new tab, or count of link from this button more than one");

                for(int i=0;i<tabs2.size();i++){
                    for(int j=0;j<tabs1.size();j++){
                      if(tabs2.get(i).equals(tabs1.get(j)))
                          tabs2.remove(i);
                    }
                }

                driver().switchTo().window(tabs2.get(0)).getCurrentUrl();
                link=driver().getCurrentUrl();
                driver().close();
                driver().switchTo().window(oldTab);
                return link;

            } catch (Exception e) {
                reporter.fail("Failure getting link from button ",  e);
                throw new RuntimeException("Failure getting link from button " );
            }
        }

        public static String getlinkFromElement(By by, int... timeout){
            try {
                clickOnElementIgnoreException(by);
                return driver().getCurrentUrl();
            }
            catch (Exception e) {
                    reporter.fail("Failure getting link from button ",  e);
                    throw new RuntimeException("Failure getting link from button " );
            }
    }


}
