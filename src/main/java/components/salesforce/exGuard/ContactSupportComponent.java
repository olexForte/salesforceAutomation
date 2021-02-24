package components.salesforce.exGuard;

import components.BasePageComponent;
import datasources.JSONConverter;
import datasources.RandomDataGenerator;
import entities.InputTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ContactSupportComponent extends BasePageComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactSupportComponent.class);

    public static String COMPONENT_NAME = "ContactSupportComponent";

    private static ContactSupportComponent instance = null;

    public static ContactSupportComponent getInstance() {
        if (instance == null)
            instance = new ContactSupportComponent();
        return instance;
    }

    public void enterFields(String subject, String description) {
        reporter.info("Set subject: "+ subject+" and description: "+description);
        setText(LOCATORS.getBy(COMPONENT_NAME,"SUBJECT_INPUT"),subject);
        setText(LOCATORS.getBy(COMPONENT_NAME,"DESCRIPTION_INPUT"),description);
    }

    public String getFieldValue(String fieldLabel) {
        reporter.info("get field: " + fieldLabel);
        return getElementText(LOCATORS.getBy(COMPONENT_NAME, "FIELD_BY_LABEL", fieldLabel));
    }

    public void submit() {
        reporter.info("click button submit");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"BUTTON_SUBMIT"));
    }

    public String getCaseNumber() {
        reporter.info("get case number");
      return  getElementText(LOCATORS.getBy(COMPONENT_NAME,"CASE_NUMBER"));
    }

    public void openCase(String caseNumber) {
        reporter.info("Open case");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"CASE_NUMBER"));
    }

    public String getRandomString(String template){
        reporter.info("Get random string for template: "+ template);
        return RandomDataGenerator.getRandomField(template,RandomDataGenerator.DEFAULT_SEPARATOR);
    }

    public boolean isMessageCaseCreatedExist(){
        reporter.info("Verification if message about case created exist ");
        if (findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"CASE_WAS_CREATED_MESSAGE"))!=null)
            return true;
        return false;
    }

    public String getTitle() {
        waitForPageToLoad();
        return driver().getTitle();
    }

    public HashMap<String, String> enterFields(String fieldsAsJson) {
        reporter.info("Set : " + fieldsAsJson);
        HashMap<String, String> mapOfFields = JSONConverter.toHashMapFromJsonString(fieldsAsJson);

        InputTypes it = InputTypes.getFromJsonString(LOCATORS.get(COMPONENT_NAME,"INPUT_FIELDS_AS_JSON"));
        return fillDataFields(mapOfFields, it); // TODO
    }

    public HashMap<String, String> getFields(HashMap<String, String> actualExpectedFields) {
        InputTypes it = InputTypes.getFromJsonString(LOCATORS.get(COMPONENT_NAME,"INPUT_FIELDS_AS_JSON"));
        return getDataFields(actualExpectedFields.keySet(), it); // TODO
    }
}
