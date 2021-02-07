package salesforce.VHT.smoke;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.MainPageComponent;
import components.salesforce.vht.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class VHT_ProofPhotosTest extends BaseUITest {

    private VHTMainPageComponent mainPageComponent = (VHTMainPageComponent) MainPageComponent.getInstance();
    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private VHTProofPhotographsComponent vHTProofPhotographsComponent = VHTProofPhotographsComponent.getInstance();


    @Test(testName = "Proof Photos page test")
    public void test(){

        HashMap<String,String> params = dataRepository.getParametersForTest("ProofPhotos");

        // login
        logIn(false);
        //open page proof Photographs
        mainPageComponent.openProofPhotographsFromTable(params.get("NAME"));

        //test ADD
        //count of expected item in cart
        int expectedNumberOfItemsInCart = headerComponent.getCountItemInCart() + 1;

        //add image to cart and check if message is correct
        vHTProofPhotographsComponent.addPictureToCart(params.get("PICTURE_NAME"));
        String actualMessage = vHTProofPhotographsComponent.getMessageFromPage();
        String expectedMessage = params.get("TEXT_ADD_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after added
        headerComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(headerComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");
        //check if button remove exist and button add doesn`t exist
        //too long
        Assert.assertTrue((!vHTProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && vHTProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));

        //test REMOVE
        //count of expected item in cart
        expectedNumberOfItemsInCart=expectedNumberOfItemsInCart-1;

        //remove image from cart and check if message is correct
        vHTProofPhotographsComponent.removePictureFromCart(params.get("PICTURE_NAME"));

        actualMessage = vHTProofPhotographsComponent.getMessageFromPage();
        expectedMessage = params.get("TEXT_REMOVE_PICTURE");
        Assert.assertEquals(actualMessage , expectedMessage);

        //check if cart count correct after remove
        headerComponent.waitForNumberOfItemsInCart(expectedNumberOfItemsInCart);
        Assert.assertEquals(headerComponent.getCountItemInCart(), expectedNumberOfItemsInCart, "Wrong number of items in cart");

        Assert.assertTrue((vHTProofPhotographsComponent.isAddButtonVisibleOnPhoto(params.get("PICTURE_NAME")) && !vHTProofPhotographsComponent.isRemoveButtonVisibleOnPhoto(params.get("PICTURE_NAME"))));
    }
}
