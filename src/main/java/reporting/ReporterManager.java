package reporting;

import java.io.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import pages.BasePage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import web.DriverProvider;
import datasources.FileManager;
import configuration.ProjectConfiguration;
import configuration.SessionManager;


/**
 * Reporter based on ExtendReport <br>
 *     ExtendReports - parent object
 */

public class ReporterManager {

    private static final LocalDateTime START_TIME = LocalDateTime.now();

    private static ReporterManager instance;
    public static ReporterManager Instance = (instance != null) ? instance : new ReporterManager();


    static public boolean testFailed = false;
    private static String IMAGES_SUBFOLDER = "img";

    public ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

    //marker of failed item
    public static final String MARKER_OF_FAILED_ITEM = "----";

    ReporterManager(){
    }

    //pool of ExtendTests - one per thread
    private static Map<Long, ExtentTest> testThread = new HashMap<Long, ExtentTest>();

    private static Set<String> failedTests = new HashSet();
    private static Set<String> passedTests = new HashSet();

    private static ExtentReports extent;

    private static final Logger logger = LoggerFactory.getLogger(ReporterManager.class);

    public static String REPORT_FILE_LOCATION = FileManager.OUTPUT_DIR + File.separator + "Report" + SessionManager.getSessionID() + ".html";

    private synchronized static ExtentReports getInstance() {
        if (extent == null) {
            //create report in target dir
            logger.info("Report creation: " + REPORT_FILE_LOCATION);
            extent = new ExtentReports(REPORT_FILE_LOCATION, true, NetworkMode.ONLINE);
            // add system info to Environment section on report dashboard
            extent.addSystemInfo(getBuildInfo());
        }
        return extent;
    }

    /**
     * Start recording of report
     * @param m
     * @param testName
     * @param testDescription
     * @return
     */
    public synchronized  Map<Long, ExtentTest> startTest(Method m, String testName, String testDescription) {
        //get current thread
        Long threadID = Thread.currentThread().getId();

        //start recording
        ExtentTest test = getInstance().startTest(testName, testDescription);
        //test.assignCategory(DriverProvider.getCurrentBrowserName());
        //assign groups to test (if specified)

        //put thread in thread pool
        testThread.put(threadID, test);
        return testThread;
    }

    /**
     * Assign selected tags to test
     * @param test Extend test
     * @param tags array of tags
     */
    private void assignTagsForTest(ExtentTest test, String[] tags){
        if(tags != null) {
            String expectedTags = ProjectConfiguration.getConfigProperty("tags");
            if( expectedTags != null)
                for (String tag : tags) {
                    for(String expectedTag : expectedTags.toLowerCase().split(","))
                        if(expectedTag.equals(tag))
                            test.assignCategory(tag);
                }
        }
    }
    /**
     * get current report (ExtendTest object)
     * @return
     */
    public synchronized static ExtentTest report() {
        ExtentTest report = null;

        //get report from thread pool
        Long threadID = Thread.currentThread().getId();
        if (testThread.containsKey(threadID)) {
            report = testThread.get(threadID);
        }
        return report;
    }

    /**
     * stop reporting by calling endTest()
     */
    public synchronized static void closeTest() {
        getInstance().endTest(report());
    }

    /**
     * close report
     */
    public synchronized static void closeReporter() {
        getInstance().flush();
    }

    /**
     * Add Final notes to report
     */
    public void addFinalStepsToReport() {
        if(SessionManager.getFinalSteps().size() > 0) {
            ExtentTest finalSteps = getInstance().startTest("Final Steps");
            for (String item : SessionManager.getFinalSteps()) {
                finalSteps.log(LogStatus.INFO, item);
            }
            getInstance().endTest(report());
        }
    }

    /**
     * Get build info for MCK:
     * branch, version, date
     * @return hashMap with specified parameters
     */
    public static Map<String,String> getBuildInfo() {
        Map<String,String> info = new HashMap<>();
        String branch = "N/A";
        String version = "N/A";
        String buildTime = "N/A";
        String subsidiary;

        String envType = ProjectConfiguration.getConfigProperty("EnvType");
        subsidiary = ProjectConfiguration.getConfigProperty("Subsidiary");
        if (subsidiary == null)
            subsidiary = getDefaultSubsidiary();

        String BUILD_INFO_BASE_URL = String.format("https://%s-my.%s.com", envType, subsidiary).replace("prod-", "");
        try {
            RestAssured.baseURI = BUILD_INFO_BASE_URL;
            RequestSpecification httpRequest = RestAssured.given();
            Response response = httpRequest.request(io.restassured.http.Method.GET, "/version.json");

            branch = response.jsonPath().get("Branch");
            version = response.jsonPath().get("Version");
            buildTime = response.jsonPath().get("BuildTime");
        } catch (Exception e) {
            System.out.println(String.format("Could not receive build info via API for %s %s", subsidiary, envType));
        }
        info.put("Subsidiary", subsidiary);
        info.put("Dashboard Branch", branch);
        info.put("Dashboard Version", version);
        info.put("Dashboard Build Time", buildTime);
        info.put("Browser", getBrowserName());

        return info;
    }

    /**
     * get subsidiary name based on LocatorsDir from config file
     * @return subsidiary name
     */
    private static String getDefaultSubsidiary() {
        return ProjectConfiguration.getConfigProperty("LocatorsDir");
    }

    /**
     * get browser name based on Driver or RemoteBrowser from config file
     * @return browser name
     */
    private static String getBrowserName() {
        String browserName = ProjectConfiguration.getConfigProperty("RemoteBrowser");
        if (browserName == null || browserName.equals("")) {
            browserName = ProjectConfiguration.getConfigProperty("Driver");
        }
        return browserName;
//        return Collections.singletonMap("Browser", browserName);
    }

    /**
     * generate TC name for reporting <br> (name of test on left panel)
     * @param m
     * @param data
     * @return
     */
    public String getTestName(Method m, Object[] data) {
        String testName = null;
        String address = null;

        //TODO leave as reference

        // if groups specified check that Group name () contains link to ticket
        //        String[] testGroups = m.getAnnotation(Test.class).groups();
        //        for (int i = 0; i < testGroups.length; i++) {
        //            if (testGroups[i].startsWith("http")) {
        //                address = testGroups[i];
        //            }
        //        }
        // if Group name contains web address - "hide" this address in Link <br>
        //testName = @Test testName()

        //String description = m.getAnnotation(Test.class).description();
        //if(description.matches(".*(C\\d\\d\\d\\d).*"))
        //    address = description.replaceAll(".*(C\\d\\d\\d\\d).*", ProjectConfiguration.getConfigProperty("TestRailUrl") + "/index.php?/cases/view/$1");

//        if (address != null) {
//            testName = "<a href=" + "\"" + address + "\""
//                    + "target=_blank alt=This test is linked to test case. Click to open it>"
//                    + m.getAnnotation(Test.class).testName() + "</a>";
//        } else {

            if(!m.getAnnotation(Test.class).testName().equals(""))
                testName = m.getAnnotation(Test.class).testName();
            else
                testName = m.getName();

            if(data != null && data.length != 0)
                testName =  testName + Arrays.asList(data)
                        .stream()
                        .map(o -> o.toString())
                        .collect(Collectors.joining(", ", "[","]"));

        // default behaviour - use method name as test name
        if (testName == null || testName.equals("")) {
            testName = m.getName();
        }
        logger.info("Unique test NAME " + testName);
        return testName;
    }


    /**
     * get test description from method (marked TestNG @Test)
     * @param m
     * @return
     */
    private String getTestDescription(Method m) {
        String testDescription = null;
        testDescription = m.getAnnotation(Test.class).description();
        if (testDescription == null || testDescription.equals("")) {
            testDescription = "";
        }
        return testDescription;
    }

    /**
     * get Group names assigned to test by Test annotation (TestNG @Test)
     * @param m
     * @return
     */
    private String[] getTestGroups(Method m) {

        String[] testGroups = new String[]{(Math.random() > 0.5? "login":"password")};//m.getAnnotation(Test.class).groups();
        if (testGroups == null || testGroups.equals("")) {
            testGroups[0] = "";
        }
        return testGroups;
    }

    /**
     * start reporting
     * @param m
     * @param data
     */
    public void startReporting(Method m, Object[] data) {
        TEST_NAME.set(getTestName(m,data));
        startTest(m, TEST_NAME.get(), getTestDescription(m));
        String testGroups = "";

        logger.info(
                "--------------------------------------------------------------------------------------------------------");
        logger.info("Started test '" + m.getName() + "'");
    }

    /**
     * stop reporting
     */
    public void stopReporting(){
        closeTest();
    }

    /**
     * stop reporting with result
     * @param result
     */
    public void stopReporting(ITestResult result) {
        logger.info("Stopping reporting");
        closeTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            failWithScreenshot("Test failed", result.getThrowable());
            failedTests.add(TEST_NAME.get());
            testFailed = true;
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            passedTests.add(TEST_NAME.get());
            pass("Test passed");
        } else {
            failedTests.add( result.getStatus() + " " + TEST_NAME.get());
            info("Test skipped");
            testFailed = true;
        }
    }

    /**
     * show info step in report
     * @param details
     */
    public void info(String details) {
        logger.info(String.valueOf(details));
        report().log(LogStatus.INFO, String.valueOf(details).replace("\n", "<br>"));
    }

    /**
     * show warn step in report
     * @param details
     */
    public void warn(String details) {
        logger.warn(String.valueOf(details));
        if(ProjectConfiguration.isPropertySet("ProcessWarnings"))
            report().log(LogStatus.WARNING, String.valueOf(details).replace("\n", "<br>"));
        else
            report().log(LogStatus.INFO, String.valueOf(details).replace("\n", "<br>"));
    }

    /**
     * show pass step in report
     * @param details
     */
    public void pass(String details) {
        logger.info(details);
        report().log(LogStatus.PASS, details.replace("\n", "<br>"));
    }

    /**
     * show failed step in report
     * @param message
     */
    public void fail(String message) {
        logger.error(message);
        report().log(LogStatus.FAIL,  message.replace("\n", "<br>"));
    }

    /**
     * show failed step in report
     * @param message text
     * @param e exception
     */
    public void fail(String message, Throwable e) {
        logger.error(message, e);
        String exceptionString = getStackTrace(e);
        report().log(LogStatus.FAIL,  (message + "<br>" + exceptionString ).replace("\n", "<br>") );
    }

    /**
     * Fail with screenshot with exception
     * @param details text
     * @param e exception
     */
    public void failWithScreenshot(String details, Throwable e) {
        String exceptionString = getStackTrace(e);
        failWithScreenshot(details + "\n\n" + exceptionString);
    }

    /**
     * fail step and add screenshot
     * @param details
     */
    public void failWithScreenshot(String details) {
        String screenshotFile;
        String message = "<pre>" + details.replace("\n", "<br>") + "</pre>";
        logger.error(details);
        report().log(LogStatus.INFO,  String.format("<b>Current URL is %s</b>", BasePage.getCurrentURL()));

        try {
            if (DriverProvider.isDriverActive()) {
                screenshotFile = takeScreenshot(DriverProvider.getCurrentDriver(), false);
                message = message + "<br><img style=\"max-width: 100%;height: auto;max-height: 100%;width: auto;\" src=\"" + IMAGES_SUBFOLDER + File.separator + screenshotFile + "\"></img><br>";
            }

        } catch (Exception e){
            logger.warn("Taking screenshot failed: ", e);
            // processing of problem with taking screenshot
        }
        report().log(LogStatus.FAIL,  message);
    }

    /**
     * fail step and add screenshot
     * @param details
     */
    public void passWithScreenshot(String details) {
        String message = "<pre>" + details + "</pre>";
        if(ProjectConfiguration.getConfigProperty("TakeScreenshotIfPassed") != null &&
                ProjectConfiguration.getConfigProperty("TakeScreenshotIfPassed").toLowerCase().equals("true")) {

            String screenshotFile;

            try {
                if (DriverProvider.isDriverActive()) {
                    screenshotFile = takeScreenshot(DriverProvider.getCurrentDriver(), true);
                    message = message + "<br><a href=\"" + IMAGES_SUBFOLDER + File.separator + screenshotFile + "\" target=_blank alt>"
                            + "SCREENSHOT" + "</a><br>";
                }
            } catch (Exception e) {
                // processing of problem with taking screenshot
            }
        }
        logger.info(details);
        report().log(LogStatus.PASS,  message);
    }

    /**
     * add step in report in follwoing format<br>
     *     table header1 - table header2<br>
     *     table1 item 1  table2 item 1<br>
     *     table1 item 2  table2 item 2<br>
     *     table1 item 3  table2 item 3<br>
     * @param table1Header
     * @param table2Header
     * @param table1Items
     * @param table2Items
     */
    public void addTableForComparison(String table1Header, String table2Header, List table1Items, List table2Items) {

        String message = "";

        message = "<div class='container'>\n" +
                "  <div class='row'>";

        message = message + "    <div class='col s5'>" +
                "  <h4>" + table1Header + "</h4>";

        for(Object itemObj : table1Items){
            String item = itemObj.toString();
            item = item.replaceAll("\n","<br>");
            if(item.matches(ReporterManager.MARKER_OF_FAILED_ITEM + ".*"))
                item = item.replace(MARKER_OF_FAILED_ITEM, "<div style='color:red'>") + "</div>";
            message = message + "<p>" + item.replaceAll("\n","<br>") + "</p>";
        }

        message = message + "</div>";

        message = message + "    <div class='col s2'>" +
                "  <h4>-</h4> </div>";

        message = message + "    <div class='col s5'>" +
                "  <h4>" + table2Header + "</h4>";

        for(Object itemObj : table2Items){
            String item = itemObj.toString();
            item = item.replaceAll("\n","<br>");
            if(item.matches(ReporterManager.MARKER_OF_FAILED_ITEM + ".*"))
                item = item.replace(MARKER_OF_FAILED_ITEM, "<div style='color:red'>") + "</div>";
            message = message + "<p>" + item + "</p>";
        }

        message = message + "</div>";

        message = message + "</div></div>";

        report().log(LogStatus.INFO, message);
    }

//    //TODO under construction
//    private String packMessage(String message) {
//        String result = message;
//
//        if (message.length() > 100) {
//            String id = Tools.getCurDateTime() + Tools.getRandomNumber(99999);
//            result = "<script>function toggle" + id + "() { if (document.getElementById('" + id + "').style.display == 'block'){document.getElementById('" + id + "').style.display='none'; } else {document.getElementById('" + id + "').style.display='block'; }};</script><button onclick='toggle" + id + "()'>!!!</button> <button onClick=\"if (document.getElementById('" + id + "').style.display == 'block'){document.getElementById('" + id + "').style.display='none'; } else {document.getElementById('" + id + "').style.display='block'; }\"  > >> </button><br>" +
//                    "<div id='" + id + "' style='display:block;word-wrap: break-word;border-style: solid;'>\n" +
//                    message + "</div>";
//        }
//
//        return result;
//    }


    /**
     * get trace as string
     * @param problem
     * @return
     */
    public static String getStackTrace(Throwable problem) {
        String resultMessage = (problem.getMessage()==null?"":problem.getMessage()) + "\n";
        ////e.toString() + "\n" + Arrays.stream(e.getStackTrace()).limit(5).map(e1->e1.toString()).collect(Collectors.joining("\n"));
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        problem.printStackTrace(printWriter);
        printWriter.close();
        resultMessage = resultMessage + "\n" + result.toString();
        try {
            result.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultMessage;
    }


    /**
     * take screenshot
     * @param driver
     * @return
     */
    public static String takeScreenshot(WebDriver driver, boolean hasPassed){
        if(hasPassed) {
            return takeScreenshot(driver, SessionManager.getSessionID() + "_" + String.valueOf(System.currentTimeMillis()), true);
        } else {
            return takeScreenshot(driver, SessionManager.getSessionID() + "_" + String.valueOf(System.currentTimeMillis()), false);
        }
    }

    /**
     * take screenshot
     * @param driver
     * @param name
     * @return
     */
    public static String takeScreenshot(WebDriver driver, String name, boolean isSuccess){
        String filename;
        if(isSuccess) {
            filename = name.contains(".png") ? name : name + "screen.png";
        } else {
            filename = name.contains(".png") ? name : name + "fail.png";
        }
        String screenshotLocation = FileManager.OUTPUT_DIR + File.separator + IMAGES_SUBFOLDER + File.separator + filename;

        //SessionManager.addScreenshotNameToSession(screenshotLocation);

        try {
            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(screenshotLocation));
        } catch (IOException e) {
            logger.error("Could not take screenshot", e);
//            e.printStackTrace();
        }
        return filename;
    }

    /**
     * Archive results
     * @return
     */
    public static String archiveResultsFiles() {
        ArrayList<String> listOfResultsFile = new ArrayList<String>();
        listOfResultsFile.add(FileManager.OUTPUT_DIR);
        listOfResultsFile.add(ReporterManager.REPORT_FILE_LOCATION);
        listOfResultsFile.addAll(Arrays.asList(SessionManager.getScreenshotNamesFromSession()));

        return FileManager.archiveFiles(listOfResultsFile);
    }

    /**
     * Add Image to results
     * @param comment
     * @param fileLocation
     */
    public void addImage(String comment, String fileLocation) {
        String message = "<pre>" + comment + "</pre>";
        logger.info(comment);

        message = message + "<br><img style=\"max-width: 100%;height: auto;max-height: 100%;width: auto;\" src=\"" + FileManager.getFileNameFromPath(fileLocation) + "\"></img><br>";
        report().log(LogStatus.INFO,  message);
    }

    /**
     * Add message with URL
     * @param message
     * @param url
     */
    public String infoAsURL(String message, String url) {
        message = "<a href=\"" + url + "\"> " + message + " </a>";
        logger.info(message);
        report().log(LogStatus.INFO,  message);
        return message;
    }


    public void addCustomScriptsAndStyles() {
        String customStyle = ".high {background: gainsboro }";
        String customJS = "addEventListener('click', changeClass ,true);\n" +
                "\n" +
                "function highlighter(e){\n" +
                "    setTimeout(changeClass, 500, e);\n" +
                "    }\n" +
                "function changeClass(e){\n" +
                "    var target = e.target;\n" +
                "if(target.getAttribute(\"class\") == 'mdi-navigation-close icon'){" +
                "           var elements = document.getElementsByClassName(\"category text-white\")\n" +
                "\t   for(var i =0 ; i < elements.length; i++){\n" +
                "\t\t\telements.item(i).classList.remove('high')\n" +
                "\t   } \n" +
                "       }\n" +
                "if(target.innerText == 'Clear Filters'){\n" +
                "           var elements = document.getElementsByClassName(\"category text-white\")\n" +
                "\t   for(var i =0 ; i < elements.length; i++){\n" +
                "\t\t\telements.item(i).classList.remove('high')\n" +
                "\t   } \n" +
                "       }" +
                " if(target.getAttribute(\"class\") == 'category text-white'){\n" +
                "\t   var text  = target.innerText\n" +
                "            var elements = document.getElementsByClassName(\"category text-white\")\n" +
                "\t   for(var i =0 ; i < elements.length; i++){\n" +
                "\t\tif(elements.item(i).innerText == text)\n" +
                "            \t\telements.item(i).classList.add('high')\n" +
                "\t\telse \n" +
                "\t\t\telements.item(i).classList.remove('high')\n" +
                "\t     } \n" +
                "}\n" +
                "}";

        String content = FileManager.getFileContent(REPORT_FILE_LOCATION);
        content = FileManager.replaceStringInFileContent(content, "<style>", "<style>" + customStyle);
        content = FileManager.replaceStringInFileContent(content, "</script>", "</script><script>" + customJS + "</script>");
        try {
            FileManager.createFile(REPORT_FILE_LOCATION, content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LocalDateTime getStartTime() {
        return START_TIME;
    }

    public Integer getPassedTestsCount() {
        return passedTests.size();
    }

    public Integer getFailedTestsCount() {
        return failedTests.size();
    }

    public String getListOfFailedTests(){
        return failedTests.stream().collect(Collectors.joining("\n"));
    }
}