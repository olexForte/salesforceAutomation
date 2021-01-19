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

   private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     *
     *
     */

    public static void applyFilter(String filter) {
        reporter.info("apply Filter "+ filter);
        if (filter == null || filter.equals(""))
            return;
        else
        {
            HashMap<String, String> fields = JSONConverter.toHashMapFromJsonString(filter);

            if(fields.containsKey("dateTo") && !fields.get("dateTo").isEmpty() && fields.get("dateTo")!=null)
                if(fields.containsKey("dateFrom") && !fields.get("dateFrom").isEmpty() && fields.get("dateFrom")!=null)
                {
                    //TODO
                    //Why need to parse and after this to do String ?
                    LocalDate dateFrom = LocalDate.parse(fields.get("dateFrom"), dateFormatter);
                    LocalDate dateTo = LocalDate.parse(fields.get("dateTo"), dateFormatter);
                    searchByDateRange(dateFrom.format(dateFormatter),dateTo.format(dateFormatter));
                }
                else
                {
                    LocalDate dateTo = LocalDate.parse(fields.get("dateTo"), dateFormatter);
                    searchByDate(dateTo.format(dateFormatter));
                }

            if(fields.containsKey("search") && !fields.get("search").isEmpty())
                searchByText(fields.get("search"));

            if(fields.containsKey("status") && !fields.get("status").isEmpty())
                searchByStatus(fields.get("status"));
        }
    }

    private static void clearDateInput(){
        reporter.info("Clear input date");
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_INPUT")).clear();
    }


    private static void clearDataInputRange(){
        reporter.info("Clear input date range");
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_TO_INPUT")).clear();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT")).clear();
    }


    public static void searchByDate(String date){
        useRangeOff();
        clearDateInput();
        reporter.info("Search by date: " + date);
        clearDateInput();
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_INPUT")).sendKeys(date);
        pressTab();
    }

    public static void searchByDateRange(String dateFrom, String dataTo) {
        useRangeOn();
        clearDataInputRange();
        reporter.info("Search by date range from "+dateFrom+" to "+dataTo);
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT")).sendKeys(dateFrom);
        findElement(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_TO_INPUT")).sendKeys(dataTo);
        pressTab();
    }

    private static void useRangeOn(){
        reporter.info("Use Range on");
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT"),SHORT_TIMEOUT)==null)
            clickOnElement(LOCATORS.getBy(COMPONENT_NAME,"TOGGLE_USE_RANGE"));
    }

    private static void useRangeOff(){
        reporter.info("Use Range off");
        if(findElementIgnoreException(LOCATORS.getBy(COMPONENT_NAME,"SEARCH_BY_DATE_FROM_INPUT"),SHORT_TIMEOUT)!=null)
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
