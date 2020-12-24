package configuration;

import com.google.gson.GsonBuilder;

import entities.FooterEntity;
import io.restassured.internal.mapping.GsonMapper;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import com.google.gson.Gson;

public class DataRepository
{
//    public List<Object[]> getListOfObjectFromJson(String dataName, Class t ){
//        List<Object[]> result = null;
//        String path = "src/test/automation/resources/data/" + ProjectConfiguration.getConfigProperty("DataDir") + "/" + dataName + ".json";
//        try {
//
//            String jsonFromFile= readFile(path);
//            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//
//
//            result = new ArrayList(Arrays.asList(gson.fromJson(jsonFromFile, t)));
//
//            return result;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return  result;
//    }

    public Object[] getListOfObjectFromJson(String dataName, Class t ){
        Object[] result = null;
        String path = "src/test/automation/resources/data/" + ProjectConfiguration.getConfigProperty("DataDir") + "/" + dataName + ".json";
        try {

            String jsonFromFile= readFile(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();


            result = (Object[]) gson.fromJson(jsonFromFile, t);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    private static String readFile(String filePath)
    {
        String content="";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }
}