package components.salesforce.common;

import components.BasePageComponent;
import datasources.DataGenerator;
import datasources.RandomDataGenerator;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

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

   public static <T> T randomDataGenerator(Class<T> type)
   {
      if(type.toString().equals("class java.lang.Integer") )
         return (T) Integer.valueOf(RandomStringUtils.random(8, false, true));

      //if(type.getClass().equals(String.class) )
      return (T) RandomStringUtils.random(8, true, true);
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

//   public static HashMap<String, String> getFieldsFromAPI(HashMap<String, String> fields) {
//      reporter.info("Get fields from api");
//      reloadPage();
//      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"));
//      for(Map.Entry<String,String> field: fields.entrySet())
//      {
//         field.setValue(getTextFromField(field.getKey()));
//      }
//      return fields;
//   }



   public static HashMap<String, String> editFields(HashMap<String, String> fields) {
      openEditForm();
      reporter.info("Edit fields with random data");
      for(Map.Entry<String,String> field: fields.entrySet())
      {
         String fieldName = field.getKey();
         String fieldTemplate = field.getValue();
         String randomData=getRandomData(fieldTemplate);
         setDataIntoField(fieldName,randomData);
         field.setValue(String.valueOf(randomData));
      }
      saveData();
      return fields;
   }


   //how work with boolean or checkbox
   public static String getRandomData(String template){
      reporter.info("Get random data for template: "+ template);
     return DataGenerator.getField(template);
   }

}
