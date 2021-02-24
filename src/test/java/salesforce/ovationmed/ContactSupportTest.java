package salesforce.ovationmed;

import components.salesforce.common.ContactSupportComponent;
import components.salesforce.common.HeaderComponent;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.HashMap;

public class ContactSupportTest extends BaseUITest {


    HeaderComponent headerComponent = HeaderComponent.getInstance();
    ContactSupportComponent contactSupportComponent = ContactSupportComponent.getInstance();

    @Test(testName = "Contact Support create case test")
    public void ContactSupportTest() {

        HashMap<String,String> params = dataRepository.getParametersForTest("ContactSupportTest");

        //login
        logIn(false);
        //open contact support page
        headerComponent.openItemFromUserMenu(params.get("PAGE_NAME"));
        Assert.assertEquals(contactSupportComponent.getTitle(),params.get("PAGE_TITLE"),"Wrong title on the page");
        //get expected subject and describe
        //String expectedSubject = params.get("SUBJECT");
        //String expectedDescription = params.get("DESCRIPTION");

        String expectedData = params.get("EXPECTED_FIELD");

        //set subject and describe
        HashMap<String, String> actualExpectedFields = contactSupportComponent.enterFields(expectedData);
        contactSupportComponent.submit();

        //verification if message about case created exist
        Assert.assertTrue(contactSupportComponent.isMessageCaseCreatedExist(),"Case isn`t existing");
        contactSupportComponent.openCase();

        //get value from field subject and describe
       // String actualSubject=contactSupportComponent.getFieldValue("Subject");
        //String actualDescription=contactSupportComponent.getFieldValue("Description");
        HashMap<String, String> actualActualFields  = contactSupportComponent.getFields(actualExpectedFields);

        Assert.assertTrue(actualActualFields.equals(actualExpectedFields),"Fields is incorrect");
    }


}
