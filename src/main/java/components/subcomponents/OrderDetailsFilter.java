package components.subcomponents;

import components.BasePageComponent;
import components.salesforce.AddressListingComponent;
import configuration.LocatorsRepository;
import datasources.JSONConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class OrderDetailsFilter {

    LocalDate dateFrom = null;
    LocalDate dateTo = null;
    String search = null;
    String status = null;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public OrderDetailsFilter(String filterDescription){

        HashMap<String, String> fileds = JSONConverter.toHashMapFromJsonString(filterDescription);

        if(fileds.containsKey("dateFrom"))
            dateFrom = LocalDate.parse(fileds.get("dateFrom"), dateFormatter);
        if(fileds.containsKey("dateTo"))
            dateTo = LocalDate.parse(fileds.get("dateTo"), dateFormatter);
        if(fileds.containsKey("search"))
            search = fileds.get("search");
        if(fileds.containsKey("status"))
            status = fileds.get("status");

    }

    public void apply(){

        if (dateFrom == null)
            if (dateTo != null)
                AddressListingComponent.searchByDate(dateTo.format(dateFormatter));
        else
            if (dateTo != null)
                AddressListingComponent.searchByDateRange(dateFrom.format(dateFormatter), dateTo.format(dateFormatter));
        if (search != null)
            AddressListingComponent.searchByText(search);
        if (status != null)
            AddressListingComponent.searchByStatus(status);
    }

}
