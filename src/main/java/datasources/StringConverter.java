package datasources;

import utils.BaseComparator;

import java.util.Locale;

public class StringConverter {


    /**
     * Remove from String special symbols
     * @param someString
     * @return String
     */
    public static String removeSpecialCharactersFromString(String someString){
        String result=someString.replaceAll("\\s","")
                .replaceAll("\\r?\\n", "")
                .replaceAll("[^a-zA-Z0-9]", " ")
                .toLowerCase();
        return result;
    }
}
