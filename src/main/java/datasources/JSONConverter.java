package datasources;

import entities.BaseEntity;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Support of JSON serialization/deserialization <br>
 *     required for Entities processing
 */
public class JSONConverter {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    /**
     * Create HashMap from Json string
     * @param jsonString
     * @return
     */
    public static final HashMap<String,String> toHashMapFromJsonString(String jsonString) {
        HashMap<String,String> jsonToObject = null;
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        jsonToObject = gson.fromJson(jsonString, type);
        return jsonToObject;
    }

    /**
     * Create Object from JSON String
     * @param classOfT
     * @param jsonString
     * @param <T>
     * @return
     */
    public static Object toObjectFromJson(String jsonString, Class classOfT) {
        return gson.fromJson(jsonString, classOfT);
    }


    /**
     * Object to JSON
     * @param objects
     * @return
     */
    public static String objectToJson(Object objects)
    {
        Gson gson = new GsonBuilder().create();
        String returnString = gson.toJson(objects);

        return returnString;
    }


    /**
     * Get List of Maps from JSON File
     * @param string
     * @return
     * @throws IOException possible exception
     */
    public static final List<HashMap<String,String>> toHashMapList(String string) {
        ArrayList<HashMap<String,String>> jsonToObject = null;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();

        jsonToObject = gson.fromJson(string, type);

        return jsonToObject;
    }

    /**
     * Get List of Maps from JSON File
     * @param file
     * @return
     * @throws IOException possible exception
     */
    public static final List<HashMap<String,String>> toHashMapListFromFile (File file) throws IOException {
        ArrayList<HashMap<String,String>> jsonToObject = null;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();

        try {
            jsonToObject = gson.fromJson(new JsonReader(new FileReader(file)), type);
        } catch (JsonSyntaxException e) {
            List<String> objects = FileUtils.readLines(file);
            objects.removeAll(Collections.singleton(""));
            String jsonAsString = "[" + Joiner.on(",").join(objects) + "]";
            jsonToObject = gson.fromJson(jsonAsString, type);
        }
        return jsonToObject;
    }

    /**
     * Transform entity to Json
     * @param object
     * @param omittedFields
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String toJson(T object, String... omittedFields) {
        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
        for (String str : omittedFields) {
            jsonObject.remove(str);
        }
        return jsonObject.toString();
    }

    /**
     * Transforms list of Entities to Json
     * @param objects
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String toJson(List<T> objects) {
        List<String> jsonList = Lists.transform(objects, object -> gson.toJsonTree(object).getAsJsonObject().toString());
        String jsonAsString = Joiner.on(",").join(jsonList);
        return "["+jsonAsString+"]";
    }

    /**
     * Create Object from JSON File
     * @param classOfT
     * @param file
     * @param <T>
     * @return
     * @throws FileNotFoundException possible exception
     */
    public static <T extends BaseEntity> T toObjectFromFile(Class<T> classOfT, File file) throws FileNotFoundException {
        return gson.fromJson(new JsonReader(new FileReader(file)), classOfT);
    }

    /**
     * Create Object from JSON String
     * @param classOfT
     * @param jsonString
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> T toObjectFromJson(Class<T> classOfT, String jsonString) {
        return gson.fromJson(jsonString, classOfT);
    }

    /**
     * Creates array of objects from JSON String
     * @param clazz
     * @param json
     * @param <T>
     * @return
     */
    public static final <T extends BaseEntity> List<T> toObjectListFromJson(final Class<T[]> clazz, final String json)
    {
        final T[] jsonToObject = new Gson().fromJson(json, clazz);
        List<T> result = new ArrayList<>(Arrays.asList(jsonToObject));
        result.removeAll(Collections.singleton(null));
        return result;
    }

//    public static final <T extends BaseEntity> List<T> toObjectListFromFile(final Class<T[]> clazz, final File file) throws IOException {
//        T[] jsonToObject = null;
//        Gson gson = new GsonBuilder().setPrettyPrinting()
//                .serializeNulls()
//                .registerTypeAdapter(Type.class, new TypeSerializer())
//                .create();
//        try {
//            jsonToObject = gson.fromJson(new JsonReader(new FileReader(file)), clazz);
//        } catch (JsonSyntaxException e){
//            List<String> objects = FileUtils.readLines(file);
//            objects.removeAll(Collections.singleton(""));
//            String jsonAsString = "["+ Joiner.on(",").join(objects)+"]";
//            jsonToObject = gson.fromJson(jsonAsString, clazz);
//        }
//        List<T> result = new ArrayList<>(Arrays.asList(jsonToObject));
//        result.removeAll(Collections.singleton(null));
//        return result;
//    }

}