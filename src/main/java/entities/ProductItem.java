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
    public HashMap<String,String> fields= null;
    public ProductItem(){

    }
}
