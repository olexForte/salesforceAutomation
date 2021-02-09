package salesforce;

import components.salesforce.common.ContactSupportComponent;
import components.salesforce.common.HeaderComponent;
import datasources.RandomDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class ContactSupportTest extends BaseUITest {


    HeaderComponent headerComponent = HeaderComponent.getInstance();
    ContactSupportComponent contactSupportComponent = ContactSupportComponent.getInstance();

    @Test(testName = "Contact Support create case test")
    public void ContactSupportTest() {

        HashMap<String,String> params = dataRepository.getParametersForTest("ContactSupport");

        //login
        logIn(false);
        //open contact support page
        headerComponent.openItemFromUserMenu(params.get("PAGE_NAME"));
        Assert.assertEquals(contactSupportComponent.getTitle(),"Contact Support"); // get from params
        //get expected subject and describe
        //String expectedSubject = params.get("SUBJECT");
        //String expectedDescription = params.get("DESCRIPTION");

        String expectedData = params.get("EXPECTED_FIELD_VALUES");

        //set subject and describe
        //contactSupportComponent.enterFields(expectedSubject,expectedDescription);
        HashMap<String, String> actualExpectedFields = contactSupportComponent.enterFields(expectedData);
        contactSupportComponent.submit();

        //verification if message about case created exist
        Assert.assertTrue(contactSupportComponent.isMessageCaseCreatedExist());

        contactSupportComponent.openCase();

        //get value from field subject and describe
       // String actualSubject=contactSupportComponent.getFieldValue("Subject");
        //String actualDescription=contactSupportComponent.getFieldValue("Description");
        HashMap<String, String> actualActualFields  = contactSupportComponent.getFields(actualExpectedFields);

        Assert.assertEquals(actualActualFields,actualExpectedFields);
    }


}
