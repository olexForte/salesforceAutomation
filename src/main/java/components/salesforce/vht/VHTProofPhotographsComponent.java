package components.salesforce.vht;

import components.BasePageComponent;
import org.openqa.selenium.By;

public class VHTProofPhotographsComponent extends BasePageComponent {
    static String COMPONENT_NAME = "ProofPhotographs";
    private static VHTProofPhotographsComponent instance = null;
    public static VHTProofPhotographsComponent  getInstance() {
        if (instance == null)
            instance = new VHTProofPhotographsComponent();

        return instance;
    }

    public  boolean isAddButtonVisibleOnPhoto(String name){
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
       return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name));
    }

    public  boolean isRemoveButtonVisibleOnPhoto(String name){
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name));
    }

    public  void addPictureToCart(String name){
        reporter.info("Add picture to cart" + name);
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
    }

    public  void removePictureFromCart(String name){
        reporter.info("Remove picture from cart " + name);
        scrollToElement(findElement(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name)));
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
    }

   public  String getMessageFromPage(){
      String result= getElementText(LOCATORS.getBy(COMPONENT_NAME,"MESSAGE_ABOUT_ADD_OR_REMOVE"),SHORT_TIMEOUT);
       reporter.info("Text of Message From page " + result);
      return result;
   }


}
