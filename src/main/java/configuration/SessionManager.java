package configuration;

import datasources.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Holds all Session related properties and TestIDs of executed tests (TODO can be used for Jira integration)
 */
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static Properties sessionProperties = new Properties();
    private static Properties sessionTests = new Properties();
    private static LinkedList<String> finalSteps = new LinkedList();

    static String STARTED_FLAG = "STARTED";
    static String PASS_FLAG = "PASS";
    static String FAIL_FLAG = "FAIL";

    static String SCREENSHOT_KEY = "SCREENSHOTS";

    /**
     * Get ID of session
     * @return
     */
    public static String getSessionID() {
        if(sessionProperties.get("SESSION_ID") == null) {
            sessionProperties.put("SESSION_ID", RandomDataGenerator.getCurDateTime());
            logger.info("Session ID: " + (String) sessionProperties.get("SESSION_ID"));
        }
        return (String) sessionProperties.get("SESSION_ID");
    }

    /**
     * Add Value to session
     * @param key
     * @param value
     */
    public static void addToSession(String key, String value){
        synchronized (SessionManager.class){
            sessionProperties.put(key, value);
        }
    }

    /**
     * Get value from session
     * @param key
     * @return
     */
    public static String getFromSession(String key){
            return String.valueOf(sessionProperties.get(key));
    }

    /**
     * Add to Session Tests
     * @param key
     * @param value
     */
    public static void addToTests(String key, String value){
        synchronized (SessionManager.class){
            sessionTests.put(key, value);
        }
    }

    /**
     * Get from Session Tests
     * @param key
     * @return
     */
    public static String getFromTests(String key){
        return String.valueOf(sessionTests.get(key));
    }

    /**
     * add test id to list of started tests
     * @param ids - list of TestRail IDs
     */
    public static void addTest(String ids){
        logger.info("Test started: " + ids);
        for(String id : ids.split(","))
            addToTests(id, STARTED_FLAG);
    }

    /**
     * mark test ass Passed in list of started tests
     * @param ids - list of TestRail IDs
     */
    public static void passTest(String ids) {
        logger.info("Test passed: " + ids);
        for(String id : ids.split(","))
            addToTests(id, PASS_FLAG);
    }
//
//    public static void updateTestStatus(String id, boolean testStatus){
//        logger.info("Test updated: " + id);
//        if(testStatus)
//            addToTests(id, PASS_FLAG);
//        else
//            addToTests(id, FAIL_FLAG);
//    }

    /**
     * returns status TRUE if tests finished
     * @param id
     * @return
     */
    public static boolean getTestStatus(String id){
        String status = getFromTests(id);
        return status.equals(PASS_FLAG);
    }

    /**
     * Get all Test IDs
     * @return
     */
    public static String[] getAllTestIDs() {
        return Arrays.copyOf(sessionTests.keySet().toArray(), sessionTests.keySet().toArray().length, String[].class);
    }

    /**
     * Add screenshot name to Session
     * @param screenshotLocation
     */
    public static void addScreenshotNameToSession(String screenshotLocation) {
        addToSession(SCREENSHOT_KEY, getFromSession(SCREENSHOT_KEY) + screenshotLocation + ";");
    }

    /**
     * Get all screenshots from session
     * @return
     */
    public static String[] getScreenshotNamesFromSession() {
       return getFromSession(SCREENSHOT_KEY).split(";");
    }

    /**
     * Get list of steps for Final step of report
     * @return
     */
    public static LinkedList<String> getFinalSteps() {
        return finalSteps;
    }

    /**
     * Add step to list of final steps
     * @param stepDescription
     */
    public static void addFinalStep(String stepDescription) {
        finalSteps.add(stepDescription);
    }
}
