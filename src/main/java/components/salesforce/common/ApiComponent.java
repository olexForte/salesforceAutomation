package components.salesforce.common;

import api.BaseAPIClient;
import components.BasePageComponent;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

public class ApiComponent extends BaseAPIClient {

    public static String COMPONENT_NAME="ApiComponent";
    public static HashMap<String,String> apiParams = dataRepository.getDefaultParametersForTest("Api");

    public static String getFieldNameByLabel(String objectName,String fieldLabel){
        reporter.info("Get field name by label"+fieldLabel+" for object "+objectName);
        String groovyPathFindFieldNameByLabel =String.format(apiParams.get("GPATH_GET_NAME_OF_FIELD_BY_LABEL"),fieldLabel);
        return getDescribe(objectName).jsonPath().get(groovyPathFindFieldNameByLabel);
    }

    //TODO Get array field, Get array value
    public static String[] getFieldsNameByLabel(String objectName,String[] fieldsLabel){
        String[] fieldsName = null;

        return  fieldsName;
    }


    public static Response getDescribe(String objectName){
        reporter.info("Get describe for object "+objectName);
        String url = String.format(apiParams.get("GET_DESCRIBE_BY_OBJECT_NAME"),objectName);
        return  BaseAPIClient.runQuery(url);
    }

    public static String getValue(String objectName,String fieldLabel,String id){
        reporter.info("Get value for field label "+fieldLabel+" for object "+objectName+" by id"+id);
        String fieldName=getFieldNameByLabel(objectName,fieldLabel);
        String url =String.format(apiParams.get("GET_FIELD_VALUE_RECORD_BY_ENTITY_BY_ID"),fieldName,objectName,id);
        String groovyPathFindValue =String.format(apiParams.get("GPATH_GET_VALUE_FROM_RECORD_BY_FIELD_NAME"),fieldName);
        return BaseAPIClient.runQuery(url).jsonPath().get(groovyPathFindValue);
    }

}
