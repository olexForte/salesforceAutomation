package components.salesforce.common;

import components.BasePageComponent;
import configuration.ProjectConfiguration;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;

public class FooterComponent extends BasePageComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FooterComponent.class);

    public static String COMPONENT_NAME = "FooterComponent";

    private static FooterComponent instance = null;

    public static FooterComponent getInstance() {

        if (instance == null)
//            switch (ProjectConfiguration.getConfigProperty("Client")){
//                case "VHT" :
//                    instance = new VHTFooterComponent(); break;
//                default:
            instance = new FooterComponent();
//            }
        return instance;
    }


    /**
     * Get value from Attribute
     * @param attribute name of attribute
     * @param attribute name of attribute
     * @return boolean
     */
    public String getValueFromAttribute(String itemName,String attribute){
        return getAttributeIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName),attribute,SHORT_TIMEOUT);
    }

    /**
     * Get link by click
     * @param itemName text in button
     * @param inNewTab boolean if link opening in new page
     * @return String
     */
    public String getLinkByClick(String itemName,boolean inNewTab){
        if(inNewTab==true)
            return getLinkByClickFromNewTab(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));
        if(inNewTab==false)
           return getLinkByClickFromElement(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));

        return null;
    }

    /**
     * Get link by click
     * @param itemName text in button
     * @param inNewTab boolean if link opening in new page
     * @return String
     */
    public String getLinkByHrefAttribute(String itemName,boolean inNewTab){
        LOGGER.info("Get link: " + itemName);
        String link=getAttributeIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName),"href");
        if(link==null)
           return getLinkByClick(itemName,inNewTab);

        return link;
    }

    //public String getLink()

}
