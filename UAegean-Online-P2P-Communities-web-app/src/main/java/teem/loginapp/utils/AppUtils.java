/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import teem.loginapp.pojo.AttributeTemplate;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikos
 */
public class AppUtils {

    private final static Logger log = LoggerFactory.getLogger(AppUtils.class);

    private final static String UAGEANIDPATTERN = "^[a-z]{3}/\\d+";

    public static Map<String, AttributeTemplate> parseStorkJSONResponse(String jsonString) throws IOException {
        ObjectMapper jmap = new ObjectMapper();
        TypeFactory typeFactory = jmap.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, AttributeTemplate.class);
        return jmap.readValue(jsonString, mapType);
    }

    public static String getEmailFromToken(String token) {
        if (token.contains("/")) {
            try {
                return token.split("/")[1];
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean checkIfValidTimestamp(String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Date parsedDate = dateFormat.parse(timestamp);
            Date beforeThirtyMinutes = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
            return parsedDate.after(beforeThirtyMinutes);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean checkUAgeanIDFormat(String uAgeanId) {

        return uAgeanId.toLowerCase().matches(UAGEANIDPATTERN);
    }

    public static String generateRandomADPass(int size) {
        char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?")).toCharArray();
        return RandomStringUtils.random(size, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
    }

}
