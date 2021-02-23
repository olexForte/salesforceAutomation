package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.JSONConverter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductItem {


    public static ProductItem toObject(String json) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            return gson.fromJson(json, ProductItem.class);
        }

    public String getCount() {
        return count;
    }

    public void setCount(String count) { this.count = count; }

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



    String count;
    String name;
    String id;
    String price;
    public HashMap<String,String> fields = new HashMap<>();

    public void addField(String field, String value) {
        fields.put(field, value);
    }

    public String getField(String field){
        return fields.get(field);
    }

    public ProductItem(){
    }
}
