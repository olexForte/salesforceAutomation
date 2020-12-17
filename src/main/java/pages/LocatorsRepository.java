package pages;

import configuration.ProjectConfiguration;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LocatorsRepository {

    public static By get(String componentName, String locator){
        String path = "src/test/automation/resources/locators/" + ProjectConfiguration.getConfigProperty("LocatorsDir") + "/" + componentName + ".properties";
        Properties locators = new Properties();
        File file = new File(path);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            locators.load(fileInput);
            String value = (String)locators.get(locator);
            if (value != null)
                return By.xpath(value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String generalPath = "src/test/automation/resources/locators/general/"  + componentName + ".properties";
        locators = new Properties();
        file = new File(generalPath);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            locators.load(fileInput);
            String value = (String)locators.get(locator);
            return By.xpath(value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
