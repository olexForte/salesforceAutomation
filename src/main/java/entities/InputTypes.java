package entities;

import datasources.JSONConverter;
import org.openqa.selenium.By;

import java.util.HashMap;

public class InputTypes {
    public static final String KEY_WORD = "$PARAMETER";
    String input;

    public String getInput() {
        return input;
    }

    public String getParentSelect() {
        return parentSelect;
    }

    public String getSelectOption() {
        return selectOption;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public String getCheckboxValue() {
        return checkboxValue;
    }

    String parentSelect;
    String selectOption;

    public void setInput(String input) {
        this.input = input;
    }

    public void setParentSelect(String parentSelect) {
        this.parentSelect = parentSelect;
    }

    public void setSelectOption(String selectOption) {
        this.selectOption = selectOption;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public void setCheckboxValue(String checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    String checkbox;
    String checkboxValue;

    public InputTypes(String input, String parentSelect, String selectOption, String checkbox, String checkboxValue) {
        this.input = input;
        this.parentSelect = parentSelect;
        this.selectOption = selectOption;
        this.checkbox = checkbox;
        this.checkboxValue = checkboxValue;
    }
    public InputTypes(){

    }

    public static InputTypes getFromSonString(String descriptionOfInputFields) {
        HashMap<String, String> fields = JSONConverter.toHashMapFromJsonString(descriptionOfInputFields);
        InputTypes result = new InputTypes();
        if(fields.containsKey("input"))
            result.setInput(fields.get("input"));
        if(fields.containsKey("select"))
            result.setParentSelect(fields.get("select"));
        if(fields.containsKey("selectOption"))
            result.setSelectOption(fields.get("selectOption"));
        if(fields.containsKey("checkbox"))
            result.setCheckbox(fields.get("checkbox"));
        if(fields.containsKey("checkboxValue"))
            result.setCheckboxValue(fields.get("checkboxValue"));

        return result;
    }
}
