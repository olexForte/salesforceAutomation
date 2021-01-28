package datasources;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    /*
   first char
   d- digital
   s- string (all symbols)
   c- string only alphabetic
   e- email
   b- boolean
   t- time
   dp - date past
   df - date future
   dc - date current
   timeFormat
   dd-M-yyyy hh:mm:ss
   ----
   second char -> count or data format
    //TODO time
    */
    public static String getField(String template){
        logger.info("Get random field for template "+template);
        String delimeter = "\\.";
        String param[] = template.split(delimeter);
        int count=0;
        String patern = "MM-dd-yyyy";
        Random random;
        int days=0;
        if(param[0].equals("dp")||param[0].equals("dc")||param[0].equals("df"))
        {
            if(param.length>1)
            {
                patern = param[1];
            }
            random = new Random();
            days = random.nextInt(10)+1;
        }
        else
            if(param.length>1)
            count= Integer.parseInt(template.replaceAll("[^0-9]", ""));

        logger.info("Create random field for parameters "+param.toString());

        switch (param[0]){
            case "dp" : return LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern(patern));
            case "df" : return LocalDateTime.now().plusDays(days).format(DateTimeFormatter.ofPattern(patern));
            case "dc" : return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patern));

            case "d" : return RandomStringUtils.random(count, false, true);
            case "s" : return RandomStringUtils.random(count, true, true);
            case "c" : return RandomStringUtils.random(count, true, false);
            case "b" : return String.valueOf(Math.random() < 0.5);
            case "e" : return RandomStringUtils.random(count, true, true)+"@i.ua";
        }
        return null;
    }
}
