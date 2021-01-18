package components.salesforce;

import components.BasePageComponent;
import org.openqa.selenium.By;

public class ProofPhotographsComponent  extends BasePageComponent {
    static String COMPONENT_NAME = "ProofPhotographs";

    //TODO
    // I think report methods position isn`t correct

    public static boolean checkIfDisplayedIconAdded (String name){
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
       return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name));
    }

    public static boolean checkIfDisplayedIconRemove (String name){
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        return isElementDisplayed(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name));
    }

    public static String addPictureToCart(String name){
        reporter.info("Add picture to cart" + name);
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        if(checkIfDisplayedIconAdded(name)==true)
        {
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"ADD_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
            return getMessageFromPage();
        }
        return "Icon 'Add' isn`t exist";
    }
//TODO
//If clickOnElement fail we wait time for getMessageFromPage
    public static String removePictureFromCart(String name){
        reporter.info("Remove picture from cart " + name);
        hoverItem(LOCATORS.getBy(COMPONENT_NAME,"IMAGE_BY_NAME",name));
        if(checkIfDisplayedIconRemove(name)==true)
        {
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"REMOVE_PICTURE_BUTTON_BY_NAME",name),SHORT_TIMEOUT);
            return getMessageFromPage();
        }
        return "Icon 'Remove' isn`t exist";
    }

   private static String getMessageFromPage(){
      String result= getElementText(LOCATORS.getBy(COMPONENT_NAME,"MESSAGE_ABOUT_ADD_OR_REMOVE"),SHORT_TIMEOUT);
       reporter.info("Text of Message From page " + result);
      return result;
   }


}
