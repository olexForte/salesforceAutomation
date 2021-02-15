package utils;

import entities.OrderItem;

import java.util.HashMap;

// comapre ordes and return differences
public class OrdersComparator {

    static public HashMap<String,String> compareOrders(OrderItem order1, OrderItem order2, TypesOfComparison type){
        HashMap<String,String> difference = new HashMap<>();

        // compare names
        //compare fields
        //.....
        if(type == TypesOfComparison.STRICT){

        } else {

        }

        // for each product
        // ...

        return difference;
    }

    enum TypesOfComparison{
        STRICT, // value was specified and equal to another value
        MEDIUM, // value equals to another value - if specified
        SIMPLE  // value looks like another value - if specified
    }
}
