package entities;

import datasources.JSONConverter;

import java.util.HashMap;

public class ProductItem {
    String name;
    String description;

    public ProductItem(){

    }
    public ProductItem(String product_description) {
        // JSON {"name" : "TITITI", "fields" : { "Color":"Bloue" }"}
        /// TODO read from string our new shiny object
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, String> fields) {
        this.fields = fields;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    HashMap<String,String> fields;
    String price;

}
