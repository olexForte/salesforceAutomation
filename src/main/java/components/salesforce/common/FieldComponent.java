package components.salesforce.common;

import components.BasePageComponent;
import org.bouncycastle.jcajce.provider.digest.MD2;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FieldComponent extends BasePageComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldComponent.class);

    public static String COMPONENT_NAME = "FieldComponent";

    private static FieldComponent instance = null;

    public static FieldComponent getInstance() {
        if (instance == null)
            instance = new FieldComponent();
        return instance;
    }


    //TODO
//
//    public HashMap<String,String> getFields(HashMap<String,String> fields){
//        for(Map.Entry<String,String> field: fields.entrySet()){
//            field.setValue(getField(field.getKey()));
//        }
//        return fields;
//    }

//    private String getField(String fieldLabel, int... timeout){
//        By getTextTypeOne = LOCATORS.getBy(COMPONENT_NAME,"GET_TEXT_BY_LABEL_1");
//        By getTextTypeTwo = LOCATORS.getBy(COMPONENT_NAME,"GET_TEXT_BY_LABEL_2");
//        By getTextTypeThree = LOCATORS.getBy(COMPONENT_NAME,"GET_TEXT_BY_LABEL_3");
//
//
//
//
//        getElementTextIgnoreException();
//    }


}
