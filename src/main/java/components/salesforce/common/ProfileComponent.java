package components.salesforce.common;

import api.BaseAPIClient;
import api.BaseRestClient;
import components.BasePageComponent;
import datasources.DataGenerator;
import datasources.RandomDataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileComponent extends BasePageComponent {

   public static String COMPONENT_NAME="ProfileComponent";

   public static String getProfileName(){
      reporter.info("Get profile name");
     return getElementText(LOCATORS.getBy(COMPONENT_NAME,"PROFILE_NAME"),SHORT_TIMEOUT);
   }

   public static void openEditForm(){
      reporter.info("Open edit form");
      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"),SHORT_TIMEOUT);
   }

   public static void saveData(){
      reporter.info("Save edited form");
      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"SAVE_BUTTON"),SHORT_TIMEOUT);
   }

   public static void setDataIntoField(String fieldName, String value){
      reporter.info("Set data "+value+" into field"+fieldName);
      setText(LOCATORS.getBy(COMPONENT_NAME,"FIELD_INPUT_BY_NAME",fieldName),value,SHORT_TIMEOUT);
   }

   public static String getTextFromField(String fieldName){
      reporter.info("Get text from field  "+fieldName);
      return getElementText(LOCATORS.getBy(COMPONENT_NAME,"FIELD_INPUT_BY_NAME",fieldName),SHORT_TIMEOUT);
   }

   public static HashMap<String, String> getFields(HashMap<String, String> fields) {
      reporter.info("Get fields");
      reloadPage();
      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"));
      for(Map.Entry<String,String> field: fields.entrySet())
      {
            field.setValue(getTextFromField(field.getKey()));
      }
      return fields;
   }

//   public static HashMap<String, String> getFieldsFromAPI(Set<String> fields) {
//      reporter.info("Get fields from API: "+fields.toString());
//      final  String apiUrl="https://cs199.salesforce.com";
//      String queryForContactTemplate ="/services/data/v50.0/query/?q=Select+%s++from+Contact+Where+id='0035500000lghLNAAY'";
//      String queryForUser ="/services/data/v50.0/query/?q=Select+Name++from+Contact+Where+id='0035500000lghLNAAY'";
//
//      for(String field: fields){
//         String queryContact=String.format(queryForContactTemplate,field.replaceAll(" ", ""));
//         BaseAPIClient.runQuery(queryContact).body().toString();
//      }
//      BaseAPIClient.runQuery(queryForContact);
//      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"));
////      for(Map.Entry<String,String> field: fields.entrySet())
////      {
////         field.setValue(getTextFromField(field.getKey()));
////      }
////      return fields;
//   }






   public static HashMap<String, String> editFields(HashMap<String, String> fields) {
      openEditForm();
      reporter.info("Edit fields with random data");
      for(Map.Entry<String,String> field: fields.entrySet())
      {
         String fieldName = field.getKey();
         String fieldTemplate = field.getValue();
         String randomData=getRandomString(fieldTemplate);
         setDataIntoField(fieldName,randomData);
         field.setValue(String.valueOf(randomData));
      }
      saveData();
      return fields;
   }


   public static String getRandomString(String template){
      reporter.info("Get random string for template: "+ template);
     return DataGenerator.getString(template);
   }
}
