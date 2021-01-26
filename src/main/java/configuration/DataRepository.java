package configuration;

import api.BaseAPIClient;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import datasources.FileManager;
import datasources.JSONConverter;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataRepository {

    // main logger
    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepository.class);

    private static DataRepository instance;
    public static DataRepository Instance = (instance != null) ? instance : new DataRepository();

    String DATA_DIR = "src/test/automation/resources/data/" + ProjectConfiguration.getConfigProperty("DataDir");

    /**
     * get object from json file
     * @param dataName file name of file
     * @param t class of object
     * @return Object what was get
     */
    public Object getObjectFromJson(String dataName, Class t ){
        Object result = null;
        try {

            String jsonFromFile= FileManager.getFileContent(FileManager.getFileFromDir(dataName, DATA_DIR));
//            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//            result = gson.fromJson(jsonFromFile, t);

            result = JSONConverter.toObjectFromJson(jsonFromFile, t);
            return result;
        } catch (Exception e) {
            LOGGER.error("fail get "+t.getName()+ "from file "+dataName+" "+e.getMessage());
        }
        return  result;
    }

    /**
     * get object from API
     * @param query search text
     * @param t class of object
     * @return Object what was get
     */
    public Object getObjectFromAPI(String query, Class t ){
        Object result = null;
        try {
            BaseAPIClient apiClient = new BaseAPIClient();
            Response response = apiClient.runQuery(query);
            String jsonFromResponce =response.jsonPath().get("records");

            result=  JSONConverter.toObjectFromJson(jsonFromResponce,t);
            return  result;

        } catch (Exception e) {
            LOGGER.error("fail get "+t.getName()+ "from api by query"+query+" "+e.getMessage());
        }
            return null;
    }

    /**
     * get HashMap from properties
     * @param testName file name
     * @return HashMap parameters from file
     */
    public HashMap<String, String> getParametersForTest(String testName) {
        File file = FileManager.getFileFromDir(testName, DATA_DIR);
        if(file.getName().contains(".properties"))
            return getParamsFromProperties(file);
        if(file.getName().contains(".json"))
            return getParamsFromJSON(file);

        return null;
    }

    /**
     * get String from file
     * @param fileName file name
     * @return String text from file
     */
    public String getContentFromFile(String fileName){
        String filePath = DATA_DIR+"/"+fileName;
        return FileManager.getFileContent(filePath);
    }

    /**
     * get HashMap parameters from file
     * @param file
     * @return HashMap parameters from file
     */
    private HashMap<String,String> getParamsFromProperties(File file){
        LOGGER.info("Load data properties file: " + file.getPath());
        HashMap<String,String> results = new HashMap<>();
        Properties props = new Properties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("File was not found " + file.getAbsolutePath(), e);
        }
        try {
            props.load(fileInput);
        } catch (IOException e) {
            LOGGER.error("Problems with properties loading " + file.getAbsolutePath(), e);
        }

        for (Map.Entry property: props.entrySet()) {
            results.put((String)property.getKey(), (String) property.getValue());
        }

        return results;
    }

    /**
     * get HashMap parameters from Json file
     * @param file
     * @return HashMap parameters from Json
     */
    private HashMap<String,String> getParamsFromJSON(File file){
        LOGGER.info("Load JSON data file: " + file.getPath());
        String jsonFromFile= FileManager.getFileContent(file);
        HashMap<String, String> result = JSONConverter.toHashMapFromJsonString(jsonFromFile);
        return result;
    }

}