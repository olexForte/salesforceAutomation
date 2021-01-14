package salesforce.VHT.underconstruction;

import configuration.DataRepository;
import entities.FooterEntity;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseUITest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VHTFooterTests extends BaseUITest {


    @DataProvider(name="Scripts to run")
   Iterator<Object[]> getScriptsToRun(){

        DataRepository dataRepository = DataRepository.Instance;
        FooterEntity[] res = (FooterEntity[])dataRepository.getObjectFromJson("footer", FooterEntity[].class);
        List<Object[]> result = new ArrayList<Object[]>();
        for(FooterEntity item : res)
            result.add(new FooterEntity[]{item});
      return result.iterator() ;// MainDataProvider.getTestObjects(INDEX_OF_TESTS);
   }




    @Test(dataProvider ="Scripts to run" )
   public void LinkCheck (FooterEntity element)  {

        System.out.println();

//        String link=element.linkOfElement;
//        String Xpath=element.textOfXpath;
//        boolean inNewTab=element.openInNewTab;

//        if(inNewTab==true)
//           Assert.assertEquals(FooterComponent.getLinkFromNewTab(By.xpath(Xpath)),link);
//        else
//            Assert.assertEquals(FooterComponent.getlinkFromElement(By.xpath(Xpath)),link);

      //  FooterComponent foter = new FooterComponent();
     //   String test =FooterComponent.getLinkFromButton(By.xpath(element.textOfXpath));

//        basePageComponent.open(home_page);
//        LoginComponent login = new LoginComponent();
//        login.loginAs("yurii.holoiad13@fortegrp.com","252846173922");


        //element.


       // LinkEntity text=(LinkEntity)gson.fromJson("[{\"entity\":\"value\"}]",LinkEntity.class);
       // FooterComponent foter = new FooterComponent();
       // Assert.assertEquals(foter.getLinkFromButton(foter.facebookButton),foter.FACEBOOK_LINK);
    }


}

