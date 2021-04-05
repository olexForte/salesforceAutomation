package components.salesforce.common;

import components.BasePageComponent;
import entities.InputTypes;
import entities.Field;

import java.util.ArrayList;
import java.util.List;

public class RegistrationComponent extends BasePageComponent {


    public static String COMPONENT_NAME = "RegistrationComponent";
    private static RegistrationComponent instance = null;


    public static RegistrationComponent getInstance() {
        if (instance == null)
            instance = new RegistrationComponent();

        return instance;
    }

    public static InputTypes inputTypes = InputTypes.getFromJsonString(LOCATORS.get(COMPONENT_NAME, "INPUT_FIELDS_AS_JSON"));

    /**
     * @param fields list fields what need to set
     * @return List<Field> list of fields with value what was set
     */
    public List<Field> setFields(List<Field> fields, int... timeout) {
        List<Field> result = fillDataFields(fields, inputTypes, timeout);
        return result;
    }

    /**
     * @param field field what need to set
     * @return Field with value what was set
     */
    public Field setField(Field field,int... timeout) {
        Field result = fillDataField(field, inputTypes, timeout);
        return result;
    }

    /**
     * @param fields List of fields what need to get
     * @return List of fields
     */
    public List<Field> getFields(List<Field> fields) {
        List<Field> results = getDataFields(fields, inputTypes, SHORT_TIMEOUT);
        return results;
    }

    /**
     * @param field field what need to get
     * @return Field
     */
    public Field getField(Field field) {
        Field result = getDataField(field, inputTypes, SHORT_TIMEOUT);
        return result;
    }

    /**
     * @param fields List of all fields that need to check on max size
     * @return List<String> fields what have not correct max size
     */
    public List<String> checkFieldLimitWithOverMaxSize(List<Field> fields) {
        List<Field> fieldsWithMaxSizeLimit = Field.getFieldsWithMaxSizeLimit(fields);
        List<String> failedField = new ArrayList<>();
        for (Field field : fieldsWithMaxSizeLimit) {
            field = Field.createRandomValueForFieldWithCount(field, field.getMaxCount() + 1);
            setField(field,SHORT_TIMEOUT);
            String actualValue = getField(field).getValue();
            if (actualValue.length() == field.getValue().length())
                failedField.add(field.getFieldLabel() + " max length =" + field.getMaxCount() + " but was saved " + actualValue.length());
        }
        return failedField;
    }

    /**
     * @param fields List of all fields that need to check on min size or on required validation
     * @return List<String> fields what have not correct min size
     */
    public List<String> checkFieldLimitWithLessMinSize(List<Field> fields) {
        String currentUrl = getCurrentURL();
        List<Field> fieldsWithMinSizeLimit = Field.getFieldsWithMinLimit(fields);
        List<String> failedField = new ArrayList<>();
        Field notValidField;
        for (Field field : fieldsWithMinSizeLimit) {
            notValidField = Field.createRandomValueForFieldWithCount(field, field.getMinCount() - 1);

            setFields(fieldsWithMinSizeLimit,SHORT_TIMEOUT);
            setField(notValidField,SHORT_TIMEOUT);
            clickSubmit();
            if (!isFieldFailed(notValidField) || checkSuccessSubmit())
                failedField.add(field.getFieldLabel());
            open(currentUrl);
        }
        return failedField;
    }

    /**
     * @param fields List with required and valid fields what need use for positive registration
     * @return boolean is account created
     */
    public boolean createAccount(List<Field> fields) {
        setFields(fields,SHORT_TIMEOUT);
        clickSubmit();
        if (checkSuccessSubmit()) {
            return true;
        }
        return false;
    }

    /**
     * @param field field what need check if this field have error
     * @return boolean is field failed
     */
    private boolean isFieldFailed(Field field) {
        if (isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "FAILED_FIELD", field.getFieldLabel()), SHORT_TIMEOUT))
            return true;

        return false;
    }

    /**
     * click button submit
     */
    public void clickSubmit() {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "SUBMIT_BUTTON"));
    }

    /**
     * check if success message is exist
     */
    public boolean checkSuccessSubmit() {
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME, "SUCCESS_SUBMIT"), DEFAULT_TIMEOUT);
    }

}
