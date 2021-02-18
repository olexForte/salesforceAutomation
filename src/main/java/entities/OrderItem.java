package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.JSONConverter;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.*;

public class OrderItem {


    String id;
    String orderNumber;
    String date;
    String status;
    String count;
    String summaryPrice;
   public List<ProductItem> products;
   public Map<String, String> fields;

    public OrderItem(){

    }

//    public static OrderItem toLowerCase(OrderItem orderItem){
//        orderItem.setStatus(orderItem.getStatus().toLowerCase());
//        orderItem.setStatus(orderItem.getStatus().toLowerCase());
//        orderItem.setStatus(orderItem.getStatus().toLowerCase());
//        orderItem.setStatus(orderItem.getStatus().toLowerCase());
//    }

    public static OrderItem toObject(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
       return gson.fromJson(json, OrderItem.class);
    }

    public OrderItem(String order_description) {
      toObject(order_description);
      HashMap<String,String> order = JSONConverter.toHashMapFromJsonString(order_description);

        for(Map.Entry<String,String> item: order.entrySet())
        {
            switch (item.getKey())
            {
                default: fields.put(item.getKey(),item.getValue());
            }
        }
    }

//    private List<ProductItem> getListOfProducts(String products){
//
//        List<ProductItem> result = null;
//        ArrayList<String> listProducts = new ArrayList<String>(Arrays.asList(products.split(",")));
//
//        for (String item : listProducts){
//            ProductItem productItem = new ProductItem(item);
//            result.add(productItem);
//        }
//        return null;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSummaryPrice() {
        return summaryPrice;
    }

    public void setSummaryPrice(String summaryPrice) {
        this.summaryPrice = summaryPrice;
    }
}
