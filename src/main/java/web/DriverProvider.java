package web;

import com.browserstack.local.Local;
import configuration.ProjectConfiguration;
import configuration.SessionManager;
import datasources.FileManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Driver provider class<br>
 *     Generates and handles interaction with WebDriver
 */
public class DriverProvider {

    public static ThreadLocal<WebDriver> instance = new ThreadLocal<WebDriver>();

    //BrowserStack local lunch support
    static Local bsLocal = new Local();
    public static final String BS_CONNECTION_URL = getBSConnectionString();

    private static String getBSConnectionString() {
        return "https://"
                + ProjectConfiguration.getConfigProperty("BSUsername") + ":"
                + ProjectConfiguration.getConfigProperty("BSAutomateKey")
                + "@hub-cloud.browserstack.com/wd/hub";
    }

    /**
     * Check if Driver is Active
     * @return is Driver active
     * @throws Exception possible exception
     */
    public static boolean isDriverActive() throws Exception {
        return (instance.get() != null);
    }

    /**
     * Get Driver instance
     * @return current Driver instance
     * @throws Exception possible exception
     */
    public static WebDriver getCurrentDriver() throws Exception {
        if (instance.get() == null)
            return null;
        return instance.get();

    }

    /**
     * Get DRiver instance with TestName (for CloudBased solutions)
     * @param testName
     * @return
     * @throws Exception possible exception
     */
    public static WebDriver getDriver(String testName) throws Exception {

        if (instance.get() == null)
            switch (getCurrentBrowserName()) {
                case "ie"  : instance.set(getIE()); break;
                case "firefox"  : instance.set(getFirefox()); break;
                // cloud based
                case "bs"       : instance.set(getBrowserStackDriver(testName)); break;
                case "bsproxy"  : instance.set(getBrowserStackDriverProxy(testName)); break;
                case "kobiton"  : instance.set(getKobitonDriver(testName));break;
                case "sl"       : instance.set(getSauceLabDriver(testName)); break;
                case "selenoid" : instance.set(getSelenoid());break;
                //chrome based
                case "proxy"    : instance.set(getChrome(false, true)); break;
                case "chrome_headless": instance.set(getChrome(true, false));break;
                case "chrome_headless_proxy": instance.set(getChrome(true, true));break;
              //
                //
                //  case "selenium4": instance.set(getSelenium4());break;
                default: instance.set(getChrome(false, false));

            }

        //return instance;
        return instance.get();
    }

//    //TODO
//    private static WebDriver getSelenium4() {
//        //.....
//        WebDriver driver = new Selenium4Wrapper(getChrome(true, true));
//        return driver;
//    }


    /**
     * Get current Browser name
     * @return
     */
    private static String getCurrentBrowserName() {
        return ProjectConfiguration.getConfigProperty("Driver").toLowerCase();
    }

    /**
     * Close Driver
     * @throws Exception possible exception
     */
    public static void closeDriver() throws Exception {

        //stop proxy if required
        BrowserProxy.stopServer();

        //stop driver
        instance.get().quit();
        instance.set(null);

        //for BrowserStack stop BS proxy
        if (ProjectConfiguration.getConfigProperty("Driver").toLowerCase().equals("bsproxy")) {
            //stop the Local instance
            bsLocal.stop();
        }
    }

    /**
     * Get Firefox driver<br>
     *     FirefoxDriverVersion config parameter used to setup version
     * @return
     */
    static public FirefoxDriver getFirefox() {

        //System.setProperty("webdriver.gecko.driver", FIREFOX_PATH);

        DesiredCapabilities caps = new DesiredCapabilities();//DesiredCapabilities.firefox();
        caps.setBrowserName("firefox");
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        synchronized (DataProvider.class) {
            if (ProjectConfiguration.getConfigProperty("FirefoxDriverVersion") == null)
                WebDriverManager.firefoxdriver().setup();
            else
                WebDriverManager.firefoxdriver().version(ProjectConfiguration.getConfigProperty("FirefoxDriverVersion")).setup();
        }

        return new FirefoxDriver(caps);

    }

    /**
     * Get IE driver<br>
     *      IEDriverVersion config parameter used to setup version
     * @return
     */
    static public InternetExplorerDriver getIE() {

        DesiredCapabilities caps = new DesiredCapabilities();//DesiredCapabilities.firefox();
        //caps.setBrowserName("ie");
        caps.setBrowserName("internet explorer");
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
//        caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,true);


        synchronized (DataProvider.class) {
            if (ProjectConfiguration.getConfigProperty("IEDriverVersion") == null)
                WebDriverManager.iedriver().setup();
            else
                WebDriverManager.iedriver().version(ProjectConfiguration.getConfigProperty("IEDriverVersion")).setup();
        }

        return new InternetExplorerDriver(caps);

    }

    /**
     * Get Chrome driver<br>
     *    ChromeDriverVersion config parameter used to setup version
     * @param headless
     * @param withProxy
     * @return
     */
    static public ChromeDriver getChrome(boolean headless, boolean withProxy){

        //preferences
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);

        chromePrefs.put("download.default_directory", FileManager.OUTPUT_DIR);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("profile.default_content_setting_values.automatic_downloads",2);
        chromePrefs.put("download.directory_upgrade", true);

        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);

        chromePrefs.put("plugins.always_open_pdf_externally", true);

        // disable flash and the PDF viewer
        chromePrefs.put("plugins.plugins_disabled", new String[] {
                "Adobe Flash Player",
                "Chrome PDF Viewer"
        });

        //possible options
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        options.addArguments("--disable-blink-features=BlockCredentialedSubresources");
        options.addArguments("--start-maximized");

        //AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
        options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
        options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770

        options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
        options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
        options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
        options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
        options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc

        if (ProjectConfiguration.getConfigProperty("Window.size") == null)
            options.addArguments("--start-maximized");
        else
            options.addArguments("--window-size="+ ProjectConfiguration.getConfigProperty("Window.size"));

        options.addArguments("--test-type");
        options.addArguments("--ignore-certificate-errors");

        options.addArguments("--lang=en");

        //download required driver
        synchronized (DataProvider.class) {
            if (ProjectConfiguration.getConfigProperty("ChromeDriverVersion") == null)
                WebDriverManager.chromedriver().setup();
            else
                WebDriverManager.chromedriver().version(ProjectConfiguration.getConfigProperty("ChromeDriverVersion")).setup();
        }

        //headless support
        if(headless) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }

        //logging preferences
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        logPrefs.enable(LogType.PROFILER, Level.INFO);
        logPrefs.enable(LogType.BROWSER, Level.INFO);
        logPrefs.enable(LogType.CLIENT, Level.INFO);
        logPrefs.enable(LogType.DRIVER, Level.INFO);
        logPrefs.enable(LogType.SERVER, Level.INFO);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        if(withProxy) {
            //start PROXY
            BrowserProxy.getInstance().startServer();
            Proxy proxy = BrowserProxy.getInstance().getProxy();
            // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
            BrowserProxy.getInstance().proxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

            options.setCapability(CapabilityType.PROXY, proxy);
        }

        //some options
        options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        options.setCapability("chrome.switches", Arrays.asList("--incognito"));
        options.setCapability(ChromeOptions.CAPABILITY, options);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        return new ChromeDriver(options);
    }




///// REMOTE  DRIVERS



    /**
     * Get instance of Selenoid Remote Driver
     * @return
     */
    public static WebDriver getSelenoid() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("75.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    URI.create("http://localhost:4444/wd/hub").toURL(),
                    capabilities
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }


    /**
     * Get BS driver with proxy
     * @param testName
     * @return
     * @throws Exception possible exception
     */
    static public WebDriver getBrowserStackDriverProxy(String testName) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();

        //System.setProperty("webdriver.chrome.driver", CHROME_PATH);

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", FileManager.OUTPUT_DIR);
        chromePrefs.put("download.prompt_for_download", false);

        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        chromePrefs.put("profile.default_content_settings.popups", 0);

        // disable flash and the PDF viewer
        chromePrefs.put("plugins.plugins_disabled", new String[] {
                "Adobe Flash Player",
                "Chrome PDF Viewer"
        });
        options.addArguments("plugins.plugins_disabled=Chrome PDF Viewer");

        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        //options.setExperimentalOption("download.default_directory", FileIO.OUTPUT_DIR);

        caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        caps.setCapability("chrome.switches", Arrays.asList("--incognito"));


        options.addArguments("--start-maximized");
        options.addArguments("--test-type");
        options.addArguments("--ignore-certificate-errors");

        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);



        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "81.0 beta");
        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "10");
        caps.setCapability("resolution", "1024x768");
        caps.setCapability("name", testName);
        caps.setCapability("project", System.getProperty("src/test/automation/config"));

        caps.setCapability("browserstack.local", "true");

        BrowserProxy.getInstance().startServer(caps);

        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        BrowserProxy.getInstance().proxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        WebDriver driver = null;


        if (ProjectConfiguration.getConfigProperty("Driver").toLowerCase().equals("bsproxy")) {
            bsLocal = new Local();

            HashMap<String, String> bsLocalArgs = new HashMap<String, String>();
            bsLocalArgs.put("key", "xCrw6DQ2nmyuoZcVpk7a");
            bsLocalArgs.put("localProxyHost", "localhost");
            bsLocalArgs.put("localProxyPort", String.valueOf(BrowserProxy.getInstance().proxyServer.getPort()));
            bsLocalArgs.put("force","true");
            bsLocalArgs.put("onlyAutomate","true");
            bsLocalArgs.put("forcelocal","true");
            bsLocalArgs.put("forceproxy","true");
            bsLocal.start(bsLocalArgs);

            System.out.println(bsLocal.isRunning());
        }

        try {
            driver = new RemoteWebDriver(new URL(BS_CONNECTION_URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        //driver.get("about://blank");

        return driver;
    }


    /**
     * Get BS remote driver (Reference https://www.browserstack.com/automate/java#rest-api)
     * @param testName
     * @return
     * @throws Exception possible exception
     */
    static public WebDriver getBrowserStackDriver(String testName) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        caps.setCapability("chrome.switches", Arrays.asList("--incognito"));


        //caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        // browser config
        caps.setCapability("browser", ProjectConfiguration.getConfigProperty("RemoteBrowser"));
        caps.setCapability("browser_version", ProjectConfiguration.getConfigProperty("RemoteBrowserVersion"));
        caps.setCapability("os", ProjectConfiguration.getConfigProperty("RemoteOS"));
        caps.setCapability("os_version", ProjectConfiguration.getConfigProperty("RemoteOSVersion"));
        caps.setCapability("resolution", ProjectConfiguration.getConfigProperty("RemoteOSResolution"));
        caps.setCapability("name", testName);
        caps.setCapability("build", SessionManager.getSessionID());
        caps.setCapability("project", System.getProperty("src/test/automation/config"));

        WebDriver driver = null;

        try {
            driver = new RemoteWebDriver(new URL(BS_CONNECTION_URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        //driver.get("about://blank");

        return driver;
    }


    /**
     * Get SL Remote driver
     * @param testName
     * @return
     * @throws Exception possible exception
     */
    static public WebDriver getSauceLabDriver(String testName) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();

        //System.setProperty("webdriver.chrome.driver", CHROME_PATH);

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", FileManager.OUTPUT_DIR);
        chromePrefs.put("download.prompt_for_download", false);

        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);

        // disable flash and the PDF viewer
        chromePrefs.put("plugins.plugins_disabled", new String[] {
                "Adobe Flash Player",
                "Chrome PDF Viewer"
        });
        options.addArguments("plugins.plugins_disabled=Chrome PDF Viewer");

        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        //options.setExperimentalOption("download.default_directory", FileIO.OUTPUT_DIR);

        caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        caps.setCapability("chrome.switches", Arrays.asList("--incognito"));


        options.addArguments("--start-maximized");
        options.addArguments("--test-type");
        options.addArguments("--ignore-certificate-errors");

        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        String sauceUserName = "oleksandr.diachuk";
        String sauceAccessKey = "9ddf176c-fec1-47b7-9a34-a94774fbef83";

        /*
         * In this section, we will configure our test to run on some specific
         * browser/os combination in Sauce Labs
         */

        //set your user name and access key to run tests in Sauce
        caps.setCapability("username", sauceUserName);

        //set your sauce labs access key
        caps.setCapability("accessKey", sauceAccessKey);

        caps.setCapability("browserName", "Chrome");
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("version", "latest");

        //set your test case name so that it shows up in Sauce Labs
        caps.setCapability("name", testName);
        caps.setCapability("build", SessionManager.getSessionID());

        WebDriver driver = null;

        try {
            driver = new RemoteWebDriver(new URL("https://ondemand.saucelabs.com:443/wd/hub"), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        //driver.get("about://blank");

        return driver;
    }

    /**
     * Get Kobiton Remote driver
     * @param testName
     * @return
     * @throws Exception possible exception
     */
    static public WebDriver getKobitonDriver(String testName) throws Exception {

        String kobitonServerUrl = "https://odyachuk:a3d812d4-5902-44d8-bb85-99dcf26c45d1@api.kobiton.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("sessionName", "Automation test session");
        capabilities.setCapability("sessionDescription", "");
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("captureScreenshots", true);
        capabilities.setCapability("browserName", "chrome");
        // The given group is used for finding devices and the created session will be visible for all members within the group.
        capabilities.setCapability("groupId", 154); // Group: Default Group
        capabilities.setCapability("deviceGroup", "KOBITON");
        // For deviceName, platformVersion Kobiton supports wildcard
        // character *, with 3 formats: *text, text* and *text*
        // If there is no *, Kobiton will match the exact text provided
        capabilities.setCapability("deviceName", "Galaxy A3 (2017)");
        capabilities.setCapability("platformVersion", "6.0.1");
        capabilities.setCapability("platformName", "Android");


        WebDriver driver = null;

        try {
            driver = new RemoteWebDriver(new URL(kobitonServerUrl), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        //driver.get("about://blank");

        return driver;
    }
}