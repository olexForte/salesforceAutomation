package components.salesforce.common;

import components.BasePageComponent;
import datasources.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

    public void openCase() {
        reporter.info("Open case");
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"CASE_NUMBER"));
    }

    public String getRandomString(String template){
        reporter.info("Get random string for template: "+ template);
        return RandomDataGenerator.getRandomField(template,"\\.");
    }

    public boolean isMessageCaseCreatedExist(){
        reporter.info("Verification if message about case created exist ");
        if (findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"CASE_WAS_CREATED_MESSAGE"))!=null)
            return true;
        return false;
    }
}
