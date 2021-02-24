package utils;

import datasources.StringConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BaseComparator {

    public static void compareFields(String fieldName, String field1, String field2, TypesOfComparison type, HashMap<String,String> differences){
        if(type.checkThatFieldSpecified){
            if(field1 ==null && field2!=null) {
                differences.put(fieldName, fieldName+" 1 was not specified");
                return;
            }

            if(field2 ==null && field1!=null){
                differences.put(fieldName, fieldName+" 2 was not specified");
                return;
            }
        }

        if(type.checkThatFieldMalformed){
            if(field1 != field2 && !field1.equals(field2))
            {
                differences.put(fieldName,field1+" doesn't equal "+field2);
                return;
            }

        }
        else {
            if (field1 != field2 && StringConverter.removeSpecialCharactersFromString(field1).equals(StringConverter.removeSpecialCharactersFromString(field2)))
            {
                differences.put(fieldName, field1+" doesn't equal "+field2);
            }
        }
    }


    public static void compareHashMap(HashMap<String,String> hashMap1, HashMap<String,String> hashMap2, TypesOfComparison type, HashMap<String,String> difference) {
        if(hashMap1==null && hashMap2!=null) {
            difference.put("Map1", "Map 1 is null");
            return;
        }

        if(hashMap2==null && hashMap1!=null){
            difference.put("Map2", "Map 2 is null");
            return;
        }

        //check missing fields
        for (Map.Entry<String, String> item1 : hashMap1.entrySet()) {
            if (type.checkThatFieldSpecified) {
                if (hashMap2.containsKey(item1.getKey())) {
                    break;
                } else {
                    difference.put(item1.getKey(), item1.getKey() + " was not specified in Map 2");
                }
            }
        }

        for (Map.Entry<String, String> item2 : hashMap2.entrySet()) {
            if (type.checkThatFieldSpecified) {
                if (hashMap1.containsKey(item2.getKey())) {
                    break;
                } else {
                    difference.put(item2.getKey(), item2.getKey() + " was not specified in Map 1");
                }
            }
        }

        //check actual values
        for (Map.Entry<String, String> item1 : hashMap1.entrySet()) {
            if (type.checkThatFieldMalformed) {

                for (Map.Entry<String, String> item2 : hashMap2.entrySet()) {
                    if (item1.getKey().equals(item2.getKey())) {
                        if (item1.getValue().equals(item2.getValue()))
                            break;
                        else
                            difference.put(item1.getKey(), item1.getValue() + " doesn't equal " + item2.getValue());
                    }
                }

            } else {
                    for (Map.Entry<String, String> item2 : hashMap2.entrySet()) {
                        if (item1.getKey().equals(item2.getKey())) {
                            String simpleString1 = StringConverter.removeSpecialCharactersFromString(item1.getValue());
                            String simpleString2 = StringConverter.removeSpecialCharactersFromString(item2.getValue());

                            if (simpleString1.equals(simpleString2))
                                break;
                            else
                                difference.put(item1.getKey(), item1.getValue() + " doesn't look like " + item2.getValue());
                        }
                    }
            }
        }
    }

    public static double priceToFloat(String price) {
        return Double.valueOf(price.replace("$","").replace(",",""));
    }


    public static String priceToString(Double value){
        return String.format("%.2f", value);
    }
}
