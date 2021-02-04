package components.salesforce.common;

import components.BasePageComponent;
import components.salesforce.vht.VHTMainPageComponent;
import components.subcomponents.FilterComponent;
import configuration.ProjectConfiguration;

public class MainPageComponent extends BasePageComponent {

    static String COMPONENT_NAME = "MainPage";
    private static MainPageComponent instance = null;
    public static MainPageComponent  getInstance() {

        if (instance == null)
            switch (ProjectConfiguration.getConfigProperty("Client")){
                case "VHT" :
                    instance = new VHTMainPageComponent(); break;
                default:
                    instance = new MainPageComponent();
            }
        return instance;
    }


    /**
     * Open item from table
     * @param productName String
     * @return void
     */
    public void openItemFromTable(String productName) {
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME, "PRODUCT_IN_TABLE_BY_TEXT", productName));
    }
}
