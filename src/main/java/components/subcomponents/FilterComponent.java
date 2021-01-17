package components.subcomponents;

import components.BasePageComponent;
import components.salesforce.AddressListingComponent;
import configuration.LocatorsRepository;
import datasources.JSONConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

//TODO
// can to do this object to static ?
public class FilterComponent extends BasePageComponent {
    static private String COMPONENT_NAME = "filterComponent";
   private static LocalDate dateFrom = null;
   private static LocalDate dateTo = null;
   private static String search = null;
   private static String status = null;

   private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     *
     *
     */
    private static void apply(){
        if (dateFrom == null)
            if (dateTo != null)
                searchByDate(dateTo.format(dateFormatter));

        else
            if (dateTo != null)
                searchByDateRange(dateFrom.format(dateFormatter), dateTo.format(dateFormatter));
        if (search != null)
            searchByText(search);
        if (status != null)
            searchByStatus(status);
    }

    public static void applyFilter(String filter) {
        reporter.info("apply Filter "+ filter);
        HashMap<String, String> fields = JSONConverter.toHashMapFromJsonString(filter);
        if (filter == null || filter.equals(""))
            return;
        else
        {
            if(fields.containsKey("dateFrom") && !fields.get("dateFrom").isEmpty() && fields.get("dateFrom")!=null)
                dateFrom = LocalDate.parse(fields.get("dateFrom"), dateFormatter);
            else
                dateFrom=null;
            if(fields.containsKey("dateTo") && !fields.get("dateTo").isEmpty() && fields.get("dateTo")!=null)
                dateTo = LocalDate.parse(fields.get("dateTo"), dateFormatter);
            else
                dateTo=null;
            if(fields.containsKey("search"))
                search = fields.get("search");
            else
                search=null;
            if(fields.containsKey("status"))
                status = fields.get("status");
            else
                status=null;
        }
        apply();
    }

    private static void clearDateInput(){
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_INPUT")).clear();
    }

    //TODO
    // divide on two separate methods: clearFrom and clearTo and use in this ???
    private static void clearDataInputRange(){
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_TO_INPUT")).clear();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT")).clear();
    }


    public static void searchByDate(String date){
        useRangeOff();
        reporter.info("Search by date: " + date);
        clearDateInput();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_INPUT")).sendKeys(date);
        pressTab();
    }

    public static void searchByDateRange(String dateFrom, String dataTo) {
        reporter.info("Search by date range from "+dateFrom+" to "+dataTo);
        useRangeOn();
        clearDataInputRange();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT")).sendKeys(dateFrom);
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_TO_INPUT")).sendKeys(dataTo);
        pressTab();
    }

    private static void useRangeOn(){
        reporter.info("Use Range on");
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT"))==null)
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"TOGGLE_USE_RANGE"));
    }

    private static void useRangeOff(){
        reporter.info("Use Range off");
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT"))!=null)
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"TOGGLE_USE_RANGE"));
    }

    public static void searchByText(String search) {
        reporter.info("Search by text "+search);
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_TEXT_INPUT")).sendKeys(search);
        pressTab(); // do need it ?
    }

    public static void searchByStatus(String status) {
        reporter.info("Search by status "+status);
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_STATUS_OPEN_DROPDOWN"));
        clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_STATUS_ELEMENT",status));
    }

}
