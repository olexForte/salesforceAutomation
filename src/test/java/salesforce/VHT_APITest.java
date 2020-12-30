package salesforce;

import org.testng.annotations.Test;
import pages.LocatorsRepository;
import utils.BaseTest;

public class VHT_APITest extends BaseTest {


@Test
   public void Check ()  {

    String apiURL = locatorsRepository.getURL("apiURLs", "API_QUERY_URL");

    }


}

