package salesforce;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.ProfileComponent;
import configuration.DataRepository;
import entities.SearchEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ProfileTest extends BaseUITest {

    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private ProfileComponent profileComponent = ProfileComponent.getInstance();

    @Test(testName = "Profile edit test")//, dataProvider ="Data for search")
    public void profileTest(){


        HashMap<String,String> fields = dataRepository.getParametersForTest("profileFields");
        HashMap<String,String> params = dataRepository.getParametersForTest("profileTest");


        //log in
        logIn(false);
        //Open profile
        headerComponent.openItemFromUserMenu("Profile");
        //check if profile name equals to config
        Assert.assertEquals(params.get("PROFILE_NAME"),profileComponent.getProfileName());
        //Update field random data and check if data was change
        HashMap<String,String> expectedResult=profileComponent.editFields(fields);
        HashMap<String,String> actualResult =profileComponent.getFields(fields);
        Assert.assertEquals(expectedResult,actualResult);
        //Get fields values from API (from contact and user)
        //test will be fail, bug, also need to add mapping field;
        HashMap<String,String> fieldsInUser =profileComponent.getFieldsFromApi(fields,"Contact",params.get("PROFILE_CONTACT_ID"));
        HashMap<String,String> fieldsINContact = profileComponent.getFieldsFromApi(fields,"User",params.get("PROFILE_USER_ID"));
        Assert.assertEquals(fieldsInUser, fieldsINContact);
    }
}
