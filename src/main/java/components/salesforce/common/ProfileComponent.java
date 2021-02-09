package components.salesforce.common;

import components.BasePageComponent;
import datasources.RandomDataGenerator;

import java.util.HashMap;
import java.util.Map;

public class ProfileComponent extends BasePageComponent {

   public static String COMPONENT_NAME="ProfileComponent";
   private static ProfileComponent instance = null;
   public static ProfileComponent  getInstance() {
      if (instance == null)
         instance = new ProfileComponent();

      return instance;
   }
   /**
    * get name from profile
    * @return String name of opened profile
    */
   public String getProfileName(){
      reporter.info("Get profile name");
     return getElementText(LOCATORS.getBy(COMPONENT_NAME,"PROFILE_NAME"));
   }

   /**
    * Open edit form
    * @return void
    */
   public void openEditForm(){
      reporter.info("Open edit form");
      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"));
   }
   /**
    * Click button "save" in edit form
    * @return void
    */
   public void saveData(){
      reporter.info("Save edited form");
      clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"SAVE_BUTTON"));
   }

   /**
    * Set data into field in edit form
    * @param fieldLabel field label
    * @param value value that need to set
    * @return void
    */
   public void setDataIntoField(String fieldLabel, String value){
      reporter.info("Set data "+value+" into field"+fieldLabel);
      setText(LOCATORS.getBy(COMPONENT_NAME,"FIELD_INPUT_BY_NAME",fieldLabel),value,SHORT_TIMEOUT);
   }

   /**
    * Get text form field in edit form
    * @param fieldLabel field name
    * @return String value from field
    */
   public String getTextFromField(String fieldLabel){
      reporter.info("Get text from field  "+fieldLabel);
      return getElementText(LOCATORS.getBy(COMPONENT_NAME,"FIELD_INPUT_BY_NAME",fieldLabel),SHORT_TIMEOUT);
   }
   /**
    * Get text form field in edit form
    * @param fields HashMap<String, String> where key - field label
    * @return HashMap<String, String> with result where key - field label , value - field value
    */
   public HashMap<String, String> getFields(HashMap<String, String> fields) {
      reporter.info("Get fields");
      reloadPage();
      openEditForm();
      for(Map.Entry<String,String> field: fields.entrySet())
      {
            field.setValue(getTextFromField(field.getKey()));
      }
      HashMap<String,String> result = new HashMap<>();
      result.putAll(fields);
      return result;
   }

   /**
    * Edit fields
    * @param fields HashMap<String, String> where key - field label, value - template for edit
    * @return HashMap<String, String> with result where key - field label , value - field value
    */
   public HashMap<String, String> editFields(HashMap<String, String> fields) {
      openEditForm();
      reporter.info("Edit fields with random data");
      for(Map.Entry<String,String> field: fields.entrySet())
      {
         String fieldLabel = field.getKey();
         String fieldTemplate = field.getValue();
         String randomData=getRandomString(fieldTemplate);
         setDataIntoField(fieldLabel,randomData);
         field.setValue(String.valueOf(randomData));
      }
      saveData();
      return fields;
   }

   /**
    * Get random value by template
    * @param template String
    * @return String random data
    */
   public String getRandomString(String template){
      reporter.info("Get random string for template: "+ template);
      return RandomDataGenerator.getRandomField(template,RandomDataGenerator.DEFAULT_SEPARATOR);
     //return DataGenerator.getString(template);
   }

   /**
    * Get field from API
    * @param fields HashMap<String, String> where key - field label
    * @param objectName String name of object
    * @param recordId String record id
    * @return HashMap<String, String> where key - field label, value - value from field
    */
   public HashMap<String, String>  getFieldsFromApi(HashMap<String, String> fields, String objectName,String recordId){
      HashMap<String,String> result = new HashMap<>();
      result.putAll(ApiComponent.getValues(objectName,fields,recordId));
      return  result;
   }

}
