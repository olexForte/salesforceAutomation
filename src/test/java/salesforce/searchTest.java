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

public class searchTest extends BaseUITest {

    @DataProvider(name = "Data for search")
    Iterator<Object[]> getDataForTest(){
        DataRepository dataRepository = DataRepository.Instance;
        SearchEntity[] search = (SearchEntity[]) dataRepository.getObjectFromJson("DataForSearch",SearchEntity[].class);
        List<Object[]> result = new ArrayList<Object[]>();
        for(SearchEntity item : search){
            //TODO pleas explain me SearchEntity[]{item} ??
            result.add(new SearchEntity[]{item});
        }
        return result.iterator();
    }

    //TODO can i change logic about after and before ?
    @Test(testName = "test search",dataProvider ="Data for search")
    public void searchTest(SearchEntity search){
        logInApplication();
        Assert.assertTrue(SearchComponent.isSearchExist());
        SearchComponent.findByQuery(search.query);
        Assert.assertTrue(SearchResultComponent.isSearchResultExist());
        Assert.assertEquals(SearchResultComponent.getCountOfResult(),search.countOfExpectedResult);
        Assert.assertTrue(SearchResultComponent.isItemsInResult(search.result));
    }
}
