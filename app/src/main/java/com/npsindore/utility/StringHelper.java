package com.npsindore.utility;
public class StringHelper {

    public static String capitalFirstLetter(String string) {
        if (!isEmpty(string)) {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }
        return "";
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0 || text.trim().length() == 0;
    }

    public static String checkString(String text,String defaultString) {
        if( text == null || text.length() == 0 || text.trim().length() == 0 || text.equals("Null")){
            return  defaultString;
        }else {
            return  text;
        }
    }



    public static int parseStringToInt(String numberString){
        int number;
        try {
            number = Integer.parseInt(numberString);
        }
        catch (NumberFormatException e)
        {
            number = 0;
        }
        return number;
    }

}
