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

public class profileTest extends BaseUITest {

//    @DataProvider(name = "Data for search")
//    Iterator<Object[]> getDataForTest(){
//        DataRepository dataRepository = DataRepository.Instance;
//        SearchEntity[] search = (SearchEntity[]) dataRepository.getObjectFromJson("DataForSearch",SearchEntity[].class);
//        List<Object[]> result = new ArrayList<Object[]>();
//        for(SearchEntity item : search){
//            //create array from one known element
//            result.add(new SearchEntity[]{item});
//        }
//        return result.iterator();
//    }


    @Test(testName = "Profile edit test")//, dataProvider ="Data for search")
    public void profileTest(){


        HashMap<String,String> fields = dataRepository.getParametersForTest("profileFields");
        HashMap<String,String> params = dataRepository.getParametersForTest("profileTest");


        //log in
        logIn(false);
        //Open profile
        HeaderComponent.openItemFromUserMenu("Profile");
        //check if profile name equals to config
        Assert.assertEquals(params.get("PROFILE_NAME"),ProfileComponent.getProfileName());
        //Update field random data and check if data was change
        Assert.assertEquals(ProfileComponent.editFields(fields),ProfileComponent.getFields(fields));
        //Get fields values from API (from contact and user)
        //test will be fail, bug, also need to add mapping field;
        Assert.assertEquals(ProfileComponent.getFieldsFromApi(fields,"Contact",params.get("PROFILE_CONTACT_ID")),
                ProfileComponent.getFieldsFromApi(fields,"User",params.get("PROFILE_USER_ID")));




    }
}
