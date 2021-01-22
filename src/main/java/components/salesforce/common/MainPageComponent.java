package components.salesforce.common;

import components.BasePageComponent;
import components.subcomponents.FilterComponent;

public class MainPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "MainPage";

    public static void openItemFromTable(String productName) {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT", productName));
    }
}
