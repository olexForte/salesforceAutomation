package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class Field {

    public Field() {

    }

    /**
     * constructor for copy field
     *
     * @param field
     * @return new field with values like in entered field
     */
    public Field(Field field) {
        fieldLabel = field.getFieldLabel();
        value = field.getValue();
        apiName = field.getApiName();
        maxCount = field.getMaxCount();
        minCount = field.getMinCount();
        fieldType = field.fieldType;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String Value) {
        this.value = Value;
    }

    public static Field[] toObjects(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(json, Field[].class);
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public String fieldLabel = "";
    public type fieldType = type.STRING;
    public int maxCount = 0;
    public int minCount = 0;
    public String apiName = "";
    public String value = "";

    public enum type {
        NUMBER("NUMBER"),
        EMAIL("EMAIL"),
        STRING("STRING"),
        DOUBLE_NUMBER("DOUBLE_NUMBER"),
        DATE("DATE"),
        DROPDOWN("DROPDOWN");

        public String getType() {
            return scope;
        }

        public void setType(String scope) {
            this.scope = scope;
        }

        String scope;

        type(String scope) {
            this.scope = scope;
        }
    }


    /**
     * get field that have max length limit
     *
     * @param fields all fields
     * @return List<Field> fields with max length limit
     */
    public static List<Field> getFieldsWithMaxSizeLimit(List<Field> fields) {
        List<Field> fieldWithMaxLimit = new ArrayList<>();
        for (Field field : fields)
            if (field.getMaxCount() > 0)
                fieldWithMaxLimit.add(field);

        return fieldWithMaxLimit;
    }

    /**
     * get field that have min length limit or is required
     *
     * @param fields all fields
     * @return List<Field> fields with min length limit or required
     */
    public static List<Field> getFieldsWithMinLimit(List<Field> fields) {
        List<Field> fieldWithMinLimit = new ArrayList<>();
        for (Field field : fields)
            if (field.getMinCount() > 0)
                fieldWithMinLimit.add(field);

        return fieldWithMinLimit;
    }

    /**
     * create random value with count for field with length of field
     *
     * @param field
     * @param countOfField
     * @return
     */
    //TODO add more types
    static public Field createRandomValueForFieldWithCount(Field field, int countOfField) {
        Field result = new Field(field);
        if (countOfField == 0)
            result.setValue("");
        else if (field.fieldType == Field.type.NUMBER)
            result.setValue(RandomDataGenerator.getRandomField(">" + countOfField + "d"));
        else if (field.fieldType == Field.type.STRING)
            result.setValue(RandomDataGenerator.getRandomField(">" + countOfField + "s"));
        else if (field.fieldType == Field.type.EMAIL)
            result.setValue(RandomDataGenerator.getRandomField(">email"));
        else if (field.fieldType == Field.type.DROPDOWN)
            result.setValue(RandomDataGenerator.getRandomField(field.getValue()));

        return result;
    }
}
