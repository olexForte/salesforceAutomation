package salesforce;

import components.salesforce.common.HeaderComponent;
import configuration.DataRepository;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class navigationMenuTest extends BaseUITest {


    @DataProvider(name="navigation")
    public Iterator<Object[]> dataProvider(){
        DataRepository dataRepository = DataRepository.Instance;
        String navigationWays = dataRepository.getContentFromFile("NavigationWays.csv");
        String[] listWays=navigationWays.split("\\n");
        List<Object[]> result = new ArrayList<Object[]>();
        for(String way : listWays){
            result.add(new String[]{way});
        }
        return result.iterator();
    }

    @Test(testName = "Navigation test", dataProvider ="navigation")
    public void navigate(Object way){
        logIn(false);
        Assert.assertEquals(HeaderComponent.navigateByHeaderMenuToItem((String) way),HeaderComponent.getNameResultPage(),"Name page result is unexpected");
    }

}
