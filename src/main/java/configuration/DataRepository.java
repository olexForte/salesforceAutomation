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
    public static String DEFAULT_CSV_SEPARATOR = "\\|";
    String DATA_DIR = "src/test/automation/resources/data/" + ProjectConfiguration.getConfigProperty("DataDir");
    public static final String DEFAULT_DATA_DIR = "src/test/automation/resources/data/default/";

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
     * Get list of lists items from file
     * @param fileName name of file in TEST_DATA_RESOURCES
     * @return List of Lists of Strings with all data from file
     * @throws Exception possible exception
     */
    public List<String[]> getTableDataFromFile(String fileName,String... separator) throws Exception {
        List<String[]> result = new LinkedList<>();
        String currentSeparator = separator.length==0 ? DEFAULT_CSV_SEPARATOR : separator[0];

        List<String> lineList = FileManager.getFileContentAsListOfLines( getDataFile(fileName));
        for(String line : lineList){
            String[] list = (line.split(currentSeparator));
            result.add(list);
        }
        return result;
    }
    /**
     * Get test file name based on dataField
     * @param dataField
     * @return file location
     */
    public File getDataFile(String dataField) throws Exception {
        LOGGER.info("Get data file from field:" + dataField);
        //DEFAULT_LOCATORS_DIR
        return FileManager.getFileFromDirs(dataField, DATA_DIR);//, DEFAULT_TEST_DATA_RESOURCES);
    }


    //TODO need to do dataRepository like Locators repository (Include default dir)
    public HashMap<String, String> getDefaultParametersForTest(String testName){
        File file = FileManager.getFileFromDir(testName, DEFAULT_DATA_DIR);
        if(file.getName().contains(".properties"))
            return getParamsFromProperties(file);
        if(file.getName().contains(".json"))
            return getParamsFromJSON(file);

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
     * get String from file
     * @param fileName file name
     * @return String text from file
     */
    public String[] getLinesFromFile(String fileName){
        String filePath = DATA_DIR+"/"+fileName;
        return FileManager.getFileContent(filePath).split("\\n");
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