package utils;

import datasources.StringConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BaseComparator {


    public static void compareFields(String fieldName, String field1, String field2, TypesOfComparison type, HashMap<String,String> differences){
        if(type.checkThatFieldSpecified){
            if(field1 ==null && field2!=null)
            {
                differences.put(fieldName,field1+" was not specified");
                return;
            }

            if(field2 ==null && field1!=null){
                differences.put(fieldName,field2+" was not specified");
                return;
            }
        }

        if(type.checkThatFieldMalformed){
            if(field1==null || field2==null || !field1.equals(field2))
            {
                differences.put(fieldName,field1+" doesn't equals "+field2);
                return;
            }

        }
        else {
            if (field1==null || field2==null ||StringConverter.removeSpecialCharactersFromString(field1).equals(StringConverter.removeSpecialCharactersFromString(field2)))
            {
                differences.put(fieldName, field1+" doesn't equals "+field2);
            }
        }
    }


    public static void compareHashMap(HashMap<String,String> hashMap1, HashMap<String,String> hashMap2, TypesOfComparison type, HashMap<String,String> difference) {
        if(hashMap1==null && hashMap2!=null)
        {
            difference.putAll(hashMap2);
            return;
        }

        if(hashMap2==null && hashMap1!=null){
            difference.putAll(hashMap1);
            return;
        }


        TreeMap<String, String> sortedHashMap1 = (hashMap1.size() >= hashMap2.size()) ? new TreeMap<>(hashMap1) : new TreeMap<>(hashMap2);
        TreeMap<String, String> sortedHashMap2 = (hashMap1.size() < hashMap2.size()) ? new TreeMap<>(hashMap1) : new TreeMap<>(hashMap2);



        for (Map.Entry<String, String> item1 : sortedHashMap1.entrySet()) {
        if (type.checkThatFieldSpecified) {
                if (sortedHashMap2.containsKey(item1.getKey())) {
                    break;
                } else {
                    difference.put(item1.getKey(), "was not specified");
                }
        }

        if (type.checkThatFieldMalformed) {

            for (Map.Entry<String, String> item2 : sortedHashMap2.entrySet()) {
                if (item1.getKey() == item2.getKey()) {
                    if (item1.getValue().equals(item2.getValue()))
                        break;
                    else
                        difference.put(item1.getKey(), item1.getValue() + " doesn't equals " + item2.getValue());
                }
            }

        } else {
                for (Map.Entry<String, String> item2 : sortedHashMap2.entrySet()) {
                    if (item1.getKey() == item2.getKey()) {
                        String simpleString1 = StringConverter.removeSpecialCharactersFromString(item1.getValue());
                        String simpleString2 = StringConverter.removeSpecialCharactersFromString(item2.getValue());

                        if (simpleString1.equals(simpleString2))
                            break;
                        else
                            difference.put(item1.getKey(), item1.getValue() + " doesn't equals " + item2.getValue());
                    }
                }
        }
    }
    }


}
