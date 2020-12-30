package pages;

import configuration.ProjectConfiguration;
import org.openqa.selenium.By;
import reporting.ReporterManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocatorsRepository {

    private static LocatorsRepository instance;
    public static LocatorsRepository Instance = (instance != null) ? instance : new LocatorsRepository();

    public static final String GENERAL_LOCATORS_DIR = "src/test/automation/resources/locators/general/";
    public static final String LOCATORS_DIR         = "src/test/automation/resources/locators/" + ProjectConfiguration.getConfigProperty("LocatorsDir") + "/";

    HashMap<String, String> processedParameters = new HashMap<>();

    public String get(String componentName, String locator){
        if (processedParameters.containsKey(componentName + "." + locator))
            return processedParameters.get(componentName + "." + locator);

        String path =  LOCATORS_DIR + componentName + ".properties";
        Properties locators = new Properties();
        File file = new File(path);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            locators.load(fileInput);

            for (Map.Entry property: locators.entrySet()) {
                processedParameters.put(componentName + "." + property.getKey(), (String) property.getValue());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (processedParameters.containsKey(componentName + "." + locator))
            return processedParameters.get(componentName + "." + locator);

        String generalPath = GENERAL_LOCATORS_DIR  + componentName + ".properties";
        locators = new Properties();
        file = new File(generalPath);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            locators.load(fileInput);

            for (Map.Entry property: locators.entrySet()) {
                processedParameters.putIfAbsent(componentName + "." + property.getKey(), (String) property.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (processedParameters.containsKey(componentName + "." + locator))
            return processedParameters.get(componentName + "." + locator);

        return null;
    }

    public By getBy(String componentName, String locator) {
        return By.xpath(get(componentName, locator));
    }

    public String getURL(String componentName, String locator){
        return get(componentName, locator);
    }
}
