package salesforce;

import components.salesforce.common.HeaderComponent;
import components.salesforce.common.ProfileComponent;
import components.salesforce.common.SearchResultComponent;
import configuration.DataRepository;
import entities.SearchEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.*;

public class SearchTest extends BaseUITest {

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
    private HeaderComponent headerComponent = HeaderComponent.getInstance();
    private SearchResultComponent searchResultComponent = SearchResultComponent.getInstance();

    @Test(testName = "test search", dataProvider ="Data for search")
    public void searchTest(SearchEntity search){

        logIn(false);
        Assert.assertTrue(headerComponent.isSearchExist());

        headerComponent.findByQuery(search.query);

        Assert.assertTrue(searchResultComponent.isSearchResultExist(), "No search results found");
        Assert.assertEquals(searchResultComponent.getTotalNumberOfResults(), search.countOfExpectedResult, "Total results number is unexpected");
        Assert.assertTrue(searchResultComponent.areItemsInResult(search.result), "Items were not found in results");
    }
}
