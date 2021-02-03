package components.salesforce.common;

import components.BasePageComponent;
import components.salesforce.vht.VHTFooterComponent;
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

    public String getLinkFromAttributeHref(String itemName){
        return getAttributeHrefIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName),SHORT_TIMEOUT);
    }

    public String getLinkByClick(String itemName,boolean inNewTab){
        if(inNewTab==true)
            return getLinkFromNewTab(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));
        if(inNewTab==false)
           return getLinkFromElement(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));

        return null;
    }

    public String getLink(String itemName,boolean inNewTab){
        LOGGER.info("Get link: " + itemName);
        String link=getLinkFromAttributeHref(itemName);
        if(link==null)
           return getLinkByClick(itemName,inNewTab);

        return link;
    }

}
