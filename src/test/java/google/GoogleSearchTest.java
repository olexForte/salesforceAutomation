package google;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.BaseTest;
import utils.Tools;

/**
 * Created by Kos on 7/17/17.
 */
public class GoogleSearchTest extends BaseTest {

    @DataProvider(name = "searches_request_provider")
    public Object[][] provider () throws Exception {
        return new String[][]{
                {"Answer to the Ultimate Question of Life, the Universe, and Everything", "42"},
                {"time in kiev", Tools.getTime()}};
    }


    @Test(dataProvider="searches_request_provider")
    public void Search(String searchRequest, String response){

        GoogleSearchPage searchPage = new GoogleSearchPage();

        searchPage.open();

        searchPage.performSearch(searchRequest);

        Assert.assertTrue(searchPage.isTextPresent(response));
    }

}
