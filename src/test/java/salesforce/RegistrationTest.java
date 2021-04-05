package salesforce;

import components.salesforce.common.LoginComponent;
import components.salesforce.common.RegistrationComponent;
import entities.Field;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegistrationTest extends BaseUITest {


    private LoginComponent loginComponent = LoginComponent.getInstance();
    private RegistrationComponent registrationComponent = RegistrationComponent.getInstance();


    /**
     * Try create account with valid values (positive test)
     */
    @Test(testName = "PositiveRegistrationTest",priority = 1)
    public void PositiveRegistrationTest() {

        logIn(true);

        HashMap<String, String> params = dataRepository.getParametersForTest("RegistrationTest");
        //get fields for registration
        List<Field> ListOfFields = new ArrayList<>(Arrays.asList(Field.toObjects(dataRepository.getContentFromFile("RegistrationFieldTest.json"))));

        registrationComponent.open(params.get("REGISTRATION_PAGE"));

        //check if can create account
        Assert.assertTrue(registrationComponent.createAccount(ListOfFields), "Account was not created");
    }

    /**
     * Test field with max length
     */
    @Test(testName = "FieldMaxSizeTest",priority = 2) //group
    public void FieldMaxSizeTest() {
        HashMap<String, String> params = dataRepository.getParametersForTest("RegistrationTest");
        //get fields for registration
        List<Field> ListOfFields = new ArrayList<>(Arrays.asList(Field.toObjects(dataRepository.getContentFromFile("RegistrationFieldTest.json"))));

        registrationComponent.open(params.get("REGISTRATION_PAGE"));

        //get fields with not correct max length validation
        String notValidField = registrationComponent.checkFieldLimitWithOverMaxSize(ListOfFields).toString();

        Assert.assertEquals("[]", notValidField, "Not valid max size of field " + notValidField);
    }

    /**
     * Test field with min/required length
     */
    @Test(testName = "FieldMinSizeTest",priority = 3) //group
    public void FieldMinSizeTest() {
        HashMap<String, String> params = dataRepository.getParametersForTest("RegistrationTest");
        //get fields for registration
        List<Field> ListOfFields = new ArrayList<>(Arrays.asList(Field.toObjects(dataRepository.getContentFromFile("RegistrationFieldTest.json"))));


        registrationComponent.open(params.get("REGISTRATION_PAGE"));
        //get fields with not correct min length
        String notValidField = registrationComponent.checkFieldLimitWithLessMinSize(ListOfFields).toString();

        Assert.assertEquals("[]", notValidField, "Not valid min size of field " + notValidField);
    }

}
