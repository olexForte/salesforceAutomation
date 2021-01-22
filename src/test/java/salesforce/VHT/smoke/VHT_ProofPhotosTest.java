package salesforce.VHT.smoke;

import components.salesforce.common.HeaderComponent;
import components.salesforce.vht.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class VHT_ProofPhotosTest extends BaseUITest {
    @Test(testName = "Proof Photos page test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("ProofPhotos");

        // login
        logInApplication();
        //open page proof Photographs
        VHTMainPageComponent.openProofPhotographsFromTable(params.get("NAME"));

        //test ADD
        //count of expected item in cart
        int expectedNumberOfItemsInCart = HeaderComponent.getCountItemInCart() + 1;

        //add image to cart and check if message is correct
        VHTProofPhotographsComponent.addPictureToCart(params.get("PICTURE_NAME"));
        String actualMessage = VHTProofPhotographsComponent.getMessageFromPage();
        String expectedMessage = params.get("TEXT_ADD_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after added
        HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
        //check if button remove exist and button add isn`t exist
        //too long
        Assert.assertTrue((!VHTProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && VHTProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));

        //test REMOVE
        //count of expected item in cart
        expectedNumberOfItemsInCart=expectedNumberOfItemsInCart-1;

        //remove image from cart and check if message is correct
        VHTProofPhotographsComponent.removePictureFromCart(params.get("PICTURE_NAME"));

        actualMessage = VHTProofPhotographsComponent.getMessageFromPage();
        expectedMessage = params.get("TEXT_REMOVE_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after remove
        HeaderComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(HeaderComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");

        Assert.assertTrue((VHTProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && !VHTProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));
    }
}
