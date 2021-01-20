package components.salesforce;

import components.BasePageComponent;
import org.openqa.selenium.By;

public class ProofPhotographsComponent  extends BasePageComponent {
    static String COMPONENT_NAME = "ProofPhotographs";

    public static boolean isAddButtonVisibleOnPhoto(String name){
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
       return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name));
    }

    public static boolean isRemoveButtonVisibleOnPhoto(String name){
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name));
    }

    public static void addPictureToCart(String name){
        reporter.info("Add picture to cart" + name);
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
    }

    public static void removePictureFromCart(String name){
        reporter.info("Remove picture from cart " + name);
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
    }

   public static String getMessageFromPage(){
      String result= getElementText(LOCATORS.getBy(COMPONENT_NAME,"MESSAGE_ABOUT_ADD_OR_REMOVE"),SHORT_TIMEOUT);
       reporter.info("Text of Message From page " + result);
      return result;
   }


}
