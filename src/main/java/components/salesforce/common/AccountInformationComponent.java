package components.salesforce.common;

import components.BasePageComponent;
import datasources.JSONConverter;
import entities.InputTypes;

import java.util.HashMap;

public class AccountInformationComponent extends BasePageComponent {

    public static String COMPONENT_NAME = "AccountInformationComponent";
    private static AccountInformationComponent instance = null;

    public static AccountInformationComponent  getInstance() {
        if (instance == null)
            instance = new AccountInformationComponent();

        return instance;
    }


    public HashMap<String,String> getFields(String fieldsAsJson){
        reporter.info("Get fields "+fieldsAsJson);
        editOpen();
        HashMap<String, String> mapOfFields = JSONConverter.toHashMapFromJsonString(fieldsAsJson);
        InputTypes it = InputTypes.getFromSonString(LOCATORS.get(COMPONENT_NAME,"INPUT_FIELDS_AS_JSON"));
        HashMap<String, String> result = getDataFields(mapOfFields.keySet(),it);
        return result;
    }


    public void editOpen(){
        reporter.info("Open edit form");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"EDIT_BUTTON"));
    }

    public void editSave(){
        reporter.info("Click button save");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"SAVE_BUTTON"));
    }


    public HashMap<String,String> setFields(String fieldsAsJson) {
        reporter.info("Set fields: " + fieldsAsJson);
        editOpen();
        HashMap<String, String> mapOfFields = JSONConverter.toHashMapFromJsonString(fieldsAsJson);

        InputTypes it = InputTypes.getFromSonString(LOCATORS.get(COMPONENT_NAME,"INPUT_FIELDS_AS_JSON"));
        HashMap<String,String> result =fillDataFields(mapOfFields, it);
        editSave();
        return result;
    }
}
