package utils;

import entities.OrderItem;
import entities.ProductItem;

import java.util.*;

public class OrdersComparator extends BaseComparator {


    String id;
    String orderNumber;
    String date;
    String status;
    String count;
    String summaryPrice;
    public List<ProductItem> products;
    public HashMap<String, String> fields;



    static public HashMap<String,String> compareOrders(OrderItem order1, OrderItem order2, TypesOfComparison type){
        HashMap<String,String> difference = new HashMap<>();


        //fields compare
        compareFields("Id",order1.getId(),order2.getId(),type,difference);
        compareFields("OrderNumber",order1.getOrderNumber(),order2.getOrderNumber(),type,difference);
        compareFields("Date",order1.getDate(),order2.getDate(),type,difference); //todo possibility check date
        compareFields("Status",order1.getStatus(),order2.getStatus(),type,difference);
        compareFields("Count",order1.getCount(),order2.getCount(),type,difference);
        compareFields("SummaryPrice",order1.getSummaryPrice(),order2.getSummaryPrice(),type,difference);
        //hashMap compare
        compareHashMap(order1.fields,order2.fields,type,difference);

        ProductComparator.compareListOfProducts(order1.products,order2.products,type,difference);


        return difference;
    }




}
