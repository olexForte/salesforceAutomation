package components.salesforce.common;

import components.BasePageComponent;
import configuration.ProjectConfiguration;
import org.openqa.selenium.By;

import javax.security.auth.login.Configuration;

public class FooterComponent extends BasePageComponent
{
   public static String COMPONENT_NAME = "FooterComponent";

    public static String getLinkFromAttributeHref(String itemName){
        return getAttributeHrefIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName),SHORT_TIMEOUT);
    }

    public static  String getLinkByClick(String itemName,boolean inNewTab){
        if(inNewTab==true)
            return getLinkFromNewTab(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));
        if(inNewTab==false)
           return getLinkFromElement(LOCATORS.getBy(COMPONENT_NAME,"LINK_BY_NAME",itemName));

        return null;
    }

    public static String getLink(String itemName,boolean inNewTab){

        String link=getLinkFromAttributeHref(itemName);
        if(link==null)
           return getLinkByClick(itemName,inNewTab);

        return link;
    }

}
