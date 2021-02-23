package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.JSONConverter;
import org.apache.commons.lang3.RandomStringUtils;
import utils.BaseComparator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderItem {


    String id;
    String orderNumber;
    String date;
    String status;
    String summaryPrice;
    public List<ProductItem> products;

    public HashMap<String,String> fields = new HashMap<>();

    public void addProduct(ProductItem value) {
        products.add(value);
    }

    public ProductItem getProduct(int index){
        return products.get(index);
    }

    public void addField(String field, String value) {
        fields.put(field, value);
    }

    public String getField(String field){
        return fields.get(field);
    }


    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, String> fields) {
        this.fields = fields;
    }

    public static OrderItem toObject(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(json, OrderItem.class);
    }

    public OrderItem() {
    }


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
        return String.valueOf(products.stream().collect(Collectors.summingInt(e -> Integer.valueOf(e.getCount()))));
    }

    public String getSummaryPrice() {

        return BaseComparator.priceToString(products.stream().collect(Collectors.summingDouble(e -> Integer.valueOf(e.getCount()) * BaseComparator.priceToFloat(e.getPrice()))));
        //return summaryPrice;
    }

    public void setSummaryPrice(String summaryPrice) {
        this.summaryPrice = summaryPrice;
    }
}
