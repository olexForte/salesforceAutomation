package salesforce;

import components.salesforce.common.FooterComponent;
import configuration.DataRepository;
import entities.FooterEntity;
import entities.SearchEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FooterTest extends BaseUITest {

    @DataProvider(name = "Item for footer")
    Iterator<Object[]> getDataForTest(){
        DataRepository dataRepository = DataRepository.Instance;
        FooterEntity[] search = (FooterEntity[]) dataRepository.getObjectFromJson("Footer",FooterEntity[].class);
        List<Object[]> result = new ArrayList<Object[]>();
        for(FooterEntity item : search){
            result.add(new FooterEntity[]{item});
        }
        return result.iterator();
    }

    private FooterComponent footerComponent = FooterComponent.getInstance();

    @Test(testName = "vtest footer links", dataProvider ="Item for footer")
    public void footerLinkTest(FooterEntity footer){
        logIn(false);
        Assert.assertEquals(footerComponent.getLinkByHrefAttribute(footer.itemName,footer.openInNewTab),footer.linkOfElement);
    }

}
