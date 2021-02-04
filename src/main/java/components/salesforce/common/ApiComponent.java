package components.salesforce.common;

import api.BaseAPIClient;
import components.BasePageComponent;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiComponent extends BaseAPIClient {

    public static String COMPONENT_NAME="ApiComponent";
    public static HashMap<String,String> apiParams = dataRepository.getDefaultParametersForTest("Api");
    public static HashMap<String,Response> describes= new HashMap<>();



    /**
     * Get field name By label
     * @param objectName name of object
     * @param fieldLabel String field label
     * @return String  field name
     */
    public static String getFieldNameByLabel(String objectName,String fieldLabel){
        reporter.info("Get field name by label "+fieldLabel+" for object "+objectName);
        String groovyPathFindFieldNameByLabel =String.format(apiParams.get("GPATH_GET_NAME_OF_FIELD_BY_LABEL"),fieldLabel);
        return getDescribe(objectName).jsonPath().get(groovyPathFindFieldNameByLabel);
    }

    /**
     * Get values record
     * @param objectName name of object
     * @param fieldLabel String field label
     * @param recordId String record id
     * @return String  field name
     */
    public static String getValue(String objectName,String fieldLabel,String recordId){
        reporter.info("Get value for field label "+fieldLabel+" for object "+objectName+" by id "+recordId);
        String fieldName=getFieldNameByLabel(objectName,fieldLabel);
        String url =defaultURL+String.format(apiParams.get("GET_FIELD_VALUE_RECORD_BY_ENTITY_BY_ID"),fieldName,objectName,recordId);
        String groovyPathFindValue =String.format(apiParams.get("GPATH_GET_VALUE_FROM_RECORD_BY_FIELD_NAME"),fieldName);
        return BaseAPIClient.runGetRequest(url).jsonPath().getString(groovyPathFindValue);
        //.get(groovyPathFindValue).toString();
    }

    /**
     * Get values record
     * @param objectName name of object
     * @param fields Hashmap<String,String> where key is field label, value will be result
     * @param recordId String record id
     * @return HashMap<String, String>  where key is field label, value will be result
     */
    public static HashMap<String, String>  getValues(String objectName,HashMap<String, String> fields, String recordId){
        for (Map.Entry<String, String> entry : fields.entrySet())
            fields.put(entry.getKey(),getValue(objectName,entry.getKey(),recordId));
        return fields;
    }

    /**
     * Get describe for object if exist, else get from API
     * @param objectName name of object
     * @return Response with describe
     */

    public static Response getDescribe(String objectName){
        if(!describes.containsKey(objectName))
            getDescribeFromApi(objectName);

        return describes.get(objectName);
    }

    /**
     * Get describe for object, and set it global HasMap<String,String> variable "describes"
     * where key= object name
     * where value= Response describe
     * @param objectName name of object
     */
    private static void getDescribeFromApi(String objectName){
        reporter.info("Get describe for object "+objectName);
        String describeUrl =defaultURL+ String.format(PARAMETERS.get("GET_DESCRIBE_BY_OBJECT_NAME"),objectName);
        describes.put(objectName,runGetRequest(describeUrl));
    }

}
