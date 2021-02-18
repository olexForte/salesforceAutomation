package utils;

import entities.OrderItem;

import java.util.HashMap;
import java.util.Locale;

// comapre ordes and return differences
public class OrdersComparator {

    static public HashMap<String,String> compareOrders(OrderItem order1, OrderItem order2, TypesOfComparison type){
        HashMap<String,String> difference = new HashMap<>();
        if(type == TypesOfComparison.STRICT){

        }
        else if(type== TypesOfComparison.MEDIUM){

        }
        else if(type== TypesOfComparison.SIMPLE){

        }


        return difference;
    }

    enum TypesOfComparison{
        STRICT(true, true, true), // value was specified and equal to another value
        MEDIUM(false, true, true), // value equals to another value - if specified
        SIMPLE(false,false,false);  // value looks like another value - if specified

        TypesOfComparison(boolean checkThatFieldSpecified, boolean checkThatFieldMalformed, boolean checkDate) {
            this.checkThatFieldSpecified = checkThatFieldSpecified;
            this.checkThatFieldMalformed = checkThatFieldMalformed;
            this.checkDate = checkDate;
        }

        boolean checkThatFieldSpecified;
        boolean checkThatFieldMalformed;
        boolean checkDate;
    }

    public OrderItem specializeFieldsOfOrder(OrderItem order){
        order.setStatus(order.getStatus().toLowerCase());

        return order;
    }



}
