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

    public static String getFieldNameByLabel(String objectName,String fieldLabel){
        reporter.info("Get field name by label "+fieldLabel+" for object "+objectName);
        String groovyPathFindFieldNameByLabel =String.format(apiParams.get("GPATH_GET_NAME_OF_FIELD_BY_LABEL"),fieldLabel);
        return getDescribe(objectName).jsonPath().get(groovyPathFindFieldNameByLabel);
    }

    public static String getValue(String objectName,String fieldLabel,String recordId){
        reporter.info("Get value for field label "+fieldLabel+" for object "+objectName+" by id "+recordId);
        String fieldName=getFieldNameByLabel(objectName,fieldLabel);
        String url =defaultURL+String.format(apiParams.get("GET_FIELD_VALUE_RECORD_BY_ENTITY_BY_ID"),fieldName,objectName,recordId);
        String groovyPathFindValue =String.format(apiParams.get("GPATH_GET_VALUE_FROM_RECORD_BY_FIELD_NAME"),fieldName);
        return BaseAPIClient.runGetRequest(url).jsonPath().getString(groovyPathFindValue);
        //.get(groovyPathFindValue).toString();
    }

//    getValue("Contact","First Name","00301000004F4lEAAS")
//    https://cs199.salesforce.com/services/data/v50.0/query/?q=Select FirstName from Contact Where id='00301000004F4lEAAS'
//    records.FirstName

    public static HashMap<String, String>  getValues(String objectName,HashMap<String, String> fields, String recordId){
        for (Map.Entry<String, String> entry : fields.entrySet())
            fields.put(entry.getKey(),getValue(objectName,entry.getKey(),recordId));
        return fields;
    }




    public static Response getDescribe(String objectName){
        if(!describes.containsKey(objectName))
            getDescribeFromApi(objectName);

        return describes.get(objectName);
    }
    private static void getDescribeFromApi(String objectName){
        reporter.info("Get describe for object "+objectName);
        String describeUrl =defaultURL+ String.format(PARAMETERS.get("GET_DESCRIBE_BY_OBJECT_NAME"),objectName);
        describes.put(objectName,runGetRequest(describeUrl));
    }

}
