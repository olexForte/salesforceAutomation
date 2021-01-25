package configuration;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocatorsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocatorsRepository.class);

    private static LocatorsRepository instance;
    public static LocatorsRepository Instance = (instance != null) ? instance : new LocatorsRepository();

    public static final String DEFAULT_LOCATORS_DIR = "src/test/automation/resources/locators/default/";
    public static final String LOCATORS_DIR         = "src/test/automation/resources/locators/" + ProjectConfiguration.getConfigProperty("LocatorsDir") + "/";

    HashMap<String, String> processedParameters = new HashMap<>();

    public String get(String componentName, String locator, String... parameters){
        if (processedParameters.containsKey(componentName + "." + locator))
            return String.format(processedParameters.get(componentName + "." + locator), parameters);

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
           // e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
        }

        if (processedParameters.containsKey(componentName + "." + locator))
            return String.format(processedParameters.get(componentName + "." + locator), parameters);

        String generalPath = DEFAULT_LOCATORS_DIR  + componentName + ".properties";
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
            return String.format(processedParameters.get(componentName + "." + locator), parameters);

        return null;
    }

    public By getBy(String componentName, String locator, String... parameters) {
        return By.xpath(get(componentName, locator, parameters));
    }

    public String getURL(String componentName, String locator, String... parameters){
        return get(componentName, locator, parameters);
    }
}
