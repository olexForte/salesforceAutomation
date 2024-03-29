package datasources;

import entities.Field;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Common tools for random data generation
 */

public class RandomDataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RandomDataGenerator.class);
    public static final String DEFAULT_SEPARATOR = "\\.";

    /**
     * encode password from Base64
     * @param encodedPassword
     * @return
     */
    public static String decodePassword(String encodedPassword) {
        //Decode password from Base64:
        return Strings.fromByteArray(Base64.getDecoder().decode(encodedPassword.getBytes()));
    }

    /**
     * decode password to Base64
     * @param password
     * @return
     */
    public static String encodePassword(String password) {
        //Encode
        return Base64.getEncoder().withoutPadding().encodeToString(password.getBytes());
    }


    /**
     * get string in format yyyyMMdd_HHmmss
     * @return
     */
    public static String getCurDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * get user email in format yyyyMMdd_HHmmss_*@gmail.com
     * @return
     */
    public static String getRandomUserEmail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_");
        return sdf.format(new Date(System.currentTimeMillis())) + String.valueOf(System.currentTimeMillis()).substring(10,13) + "@gmail.com";
    }

    /**
     * get random number in range 0 - i
     * @param i
     * @return
     */
    public static String getRandomNumber(int i) {
        Random r = new Random();
        return String.valueOf(r.nextInt(i));
    }

    public static String getRandomDropdown(List<String> elements){
        if(elements.size()>=1)
        {
            Random random = new Random();
            int randomIndex = random.nextInt(elements.size());
            return  elements.get(randomIndex);
        }
        return "";
    }

    public static String getRandomBoolean(){
      return   String.valueOf(Math.random() < 0.5);
    }


//TODO get random dropdown from webElement / from list /add possibility use some text +template

    /**
     * Generate random string for parameters
     * @param parameters
     * @return
     */
    public static String getRandomField(String parameters) {
        return getRandomField(parameters, DEFAULT_SEPARATOR);
    }
    public static String getRandomField(String parameters, String parameterDelimiter) {


        if(!parameters.contains(">")) {
            return parameters;
        }
        else
            parameters=parameters.replace(">","");

        String DEEP_PAST_DATE_LABEL = "deep_past_date";
        String CURRENT_DATE_LABEL = "current_date";
        String EMAIL_LABEL = "email";
        String FUTURE_DATE_LABEL = "future_date";
        String PAST_DATE_LABEL = "past_date";
        String WI_USER_LABEL = "wi_user";
        String DEFAULT_TEMPLATE = "10c";
        String DROPDOWNS = "dropdown";
        String CHECKBOX="randomCheckBox";




        if (parameters.toLowerCase().contains(DROPDOWNS)) {

            List<String> elements = Arrays.asList(parameters.split(parameterDelimiter));
            return getRandomDropdown(elements.subList(1,elements.size()));
        }

        if(parameters.contains(CHECKBOX)){
           return getRandomBoolean();
        }

        //dates generation

        if (parameters.toLowerCase().contains(DEEP_PAST_DATE_LABEL)) {
            int days = 73000;
            return LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }

        if (parameters.toLowerCase().contains(CURRENT_DATE_LABEL)) {
            String[] parts = parameters.split(parameterDelimiter);
            if(parts.length > 1) {
                if (parts[1].equals("ISO_OFFSET_DATE_TIME")) { // TODO generate from label
                    return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } else {
                    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(parts[1]));
                }
            }
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }

        if (parameters.toLowerCase().contains(FUTURE_DATE_LABEL)) {
            Random r = new Random();
            int days = r.nextInt(10);
            String[] parts = parameters.split(parameterDelimiter);
            if(parts.length > 1) {
                if (parts[1].equals("ISO_OFFSET_DATE_TIME")) { // TODO generate from label
                    return ZonedDateTime.now().plusDays(days).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } else {
                    return LocalDateTime.now().plusDays(days).format(DateTimeFormatter.ofPattern(parts[1]));
                }
            }
            return LocalDateTime.now().plusDays(days).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }
        if (parameters.toLowerCase().contains(PAST_DATE_LABEL)) {
            Random r = new Random();
            int days = r.nextInt(10)+1;
            String[] parts = parameters.split(parameterDelimiter);
            if(parts.length > 1) {
                if (parts[1].equals("ISO_OFFSET_DATE_TIME")) { // TODO generate from label
                    return ZonedDateTime.now().minusDays(days).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } else {
                    return LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern(parts[1]));
                }
            }
            return LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }


        // email generation
        if (parameters.toLowerCase().contains(EMAIL_LABEL))
            return getRandomStringByTemplate(DEFAULT_TEMPLATE) + "@colibri.com";

        // WI user generation
        if (parameters.toLowerCase().contains(WI_USER_LABEL)) {
            String[] parts = parameters.split(parameterDelimiter);
            if(parts.length > 1) {
                return String.format("WI_%s_%s", parts[1], getRandomStringByTemplate(DEFAULT_TEMPLATE));
            }
            return String.format("WI_notSpecified_%s", getRandomStringByTemplate(DEFAULT_TEMPLATE));
        }

        return getRandomStringByTemplate(parameters);
    }


    /**
     * Simple random string generator <br>
     *     Based on simple template: {number}{type} <br>
     *   Available types: <ol>
     *       <li>d - digit</li>
     *       <li>c - chars</li>
     *       <li>s - chars + digits</li>
     *       <li>a - any printable</li>
     *   </ol>
     *   Example: Single char: c, <br> SSN: 3d-2d-4d, <br>Name: 10c, <br>Password: 10a
     * @param template
     * @return
     */
    public static  String getRandomStringByTemplate(String template) {
        String resultString = "";
        String counter = "";
        for(int i =0; i<template.length(); i++){
            char b = template.toCharArray()[i];
            switch (b){
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                    counter = counter + b;
                    break;
                //digit
                case 'd': resultString = resultString + (counter.equals("")? "d" : RandomStringUtils.random(Integer.valueOf(counter), false, true)); counter = ""; break;
                //char
                case 'c': resultString = resultString + (counter.equals("")? "c" : RandomStringUtils.random(Integer.valueOf(counter), true, false)); counter = ""; break;
                //alphanumeric
                case 's': resultString = resultString + (counter.equals("")? "s" : RandomStringUtils.random(Integer.valueOf(counter), true, true)); counter = ""; break;
                //any
                case 'a': resultString = resultString + (counter.equals("") ? "a" : RandomStringUtils.randomPrint(Integer.valueOf(counter))); counter = ""; break;
                default: resultString = resultString + b;
            }
        }
        return resultString;
    }
}
