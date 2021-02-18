package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.JSONConverter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductItem {


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, String> fields) {
        this.fields = fields;
    }

    String count;
    String name;
    String id;
    String price;
    HashMap<String,String> fields= null;




    public ProductItem(){

    }
    public ProductItem(String product_description) {
        toObject(product_description);
        HashMap<String,String> product = JSONConverter.toHashMapFromJsonString(product_description);
        for(Map.Entry<String,String> item: product.entrySet())
        {
            switch (item.getKey())
            {
                case "id": setId(item.getValue());
                case "name": setName(item.getValue());
                case "price": setPrice(item.getValue());
                case "count": setCount(item.getValue());
                default: fields.put(item.getKey(),item.getValue());
            }
        }
    }

    public static OrderItem toObject(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.fromJson(json, OrderItem.class);
    }
}
