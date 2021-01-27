package salesforce;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.SearchComponent;
import components.salesforce.common.SearchResultComponent;
import configuration.DataRepository;
import entities.SearchEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchTest extends BaseUITest {

    @DataProvider(name = "Data for search")
    Iterator<Object[]> getDataForTest(){
        DataRepository dataRepository = DataRepository.Instance;
        SearchEntity[] search = (SearchEntity[]) dataRepository.getObjectFromJson("DataForSearch",SearchEntity[].class);
        List<Object[]> result = new ArrayList<Object[]>();
        for(SearchEntity item : search){
            //create array from one known element
            result.add(new SearchEntity[]{item});
        }
        return result.iterator();
    }

    @Test(testName = "test search", dataProvider ="Data for search")
    public void searchTest(SearchEntity search){

        logInApplication();
        Assert.assertTrue(SearchComponent.isSearchExist());

        SearchComponent.findByQuery(search.query);

        Assert.assertTrue(SearchResultComponent.isSearchResultExist(), "No search results found");
        Assert.assertEquals(SearchResultComponent.getTotalNumberOfResults(), search.countOfExpectedResult, "Total results number is unexpected");
        Assert.assertTrue(SearchResultComponent.areItemsInResult(search.result), "Items were not found in results");
    }
}
