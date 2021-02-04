package salesforce;

import components.salesforce.common.FooterComponent;
import components.salesforce.common.HeaderComponent;
import configuration.DataRepository;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NavigationMenuTest extends BaseUITest {


    @DataProvider(name="navigation")
    public Iterator<Object[]> dataProvider() throws Exception {
        DataRepository dataRepository = DataRepository.Instance;
        List<String[]> listWays = dataRepository.getTableDataFromFile("NavigationWays.csv");

        List<Object[]> result = new ArrayList<Object[]>();
        for(String[] way : listWays){
            result.add(way);
        }
        return result.iterator();
    }

    private HeaderComponent headerComponent = HeaderComponent.getInstance();

    @Test(testName = "Navigation test", dataProvider ="navigation")
    public void navigate(String location ,String result){
        logIn(false);
        headerComponent.navigateByHeaderMenuToItem(location);
        Assert.assertTrue(HeaderComponent.isElementDisplayed(result));
    }

}
