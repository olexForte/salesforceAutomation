package configuration;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import datasources.FileManager;
import datasources.JSONConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataRepository {

    // main logger
    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepository.class);

    private static DataRepository instance;
    public static DataRepository Instance = (instance != null) ? instance : new DataRepository();

    String DATA_DIR = "src/test/automation/resources/data/" + ProjectConfiguration.getConfigProperty("DataDir");

    public Object getObjectFromJson(String dataName, Class t ){
        Object result = null;
        try {

            String jsonFromFile= FileManager.getFileContent(FileManager.getFileFromDir(dataName, DATA_DIR));
//            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//            result = gson.fromJson(jsonFromFile, t);

            result = JSONConverter.toObjectFromJson(jsonFromFile, t);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public HashMap<String, String> getParametersForTest(String testName) {
        File file = FileManager.getFileFromDir(testName, DATA_DIR);
        if(file.getName().contains(".properties"))
            return getParamsFromProperties(file);
        if(file.getName().contains(".json"))
            return getParamsFromJSON(file);


        return null;
    }

    private HashMap<String,String> getParamsFromProperties(File file){

        HashMap<String,String> results = new HashMap<>();
        Properties props = new Properties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("File was not found " + file.getAbsolutePath(), e);
        }
        try {
            props.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Problems with properties loading " + file.getAbsolutePath(), e);
        }

        for (Map.Entry property: props.entrySet()) {
            results.put((String)property.getKey(), (String) property.getValue());
        }

        return results;
    }

    private HashMap<String,String> getParamsFromJSON(File file){
        String jsonFromFile= FileManager.getFileContent(file);
        HashMap<String, String> result = JSONConverter.toHashMapFromJsonString(jsonFromFile);
        return result;
    }

}