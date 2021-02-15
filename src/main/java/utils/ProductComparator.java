package utils;

import entities.OrderItem;
import entities.ProductItem;

import java.util.HashMap;

// comapre ordes and return differences
public class ProductComparator {

    static public HashMap<String,String> compareProducts(ProductItem productItem1, ProductItem productItem2, TypesOfComparison type){
        HashMap<String,String> difference = new HashMap<>();

        // compare names
        //compare fields
        //.....
        //if(type.checkDate){

        //} else {

        //}

        //TypesOfComparison.valueOf()

        return difference;
    }

    public enum TypesOfComparison{
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

}
