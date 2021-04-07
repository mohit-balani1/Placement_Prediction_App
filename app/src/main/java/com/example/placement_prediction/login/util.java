package com.example.placement_prediction.login;

import java.util.regex.Pattern;

public class util {
    public static final Pattern PASSWORD_PATTERN;

    static {
        PASSWORD_PATTERN = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 4 characters
                "$");
    }
    public static final Pattern PASSWORD_UPPERCASE_PATTERN =
            Pattern.compile("(?=.*[A-Z])" + ".{0,}");

    public static final Pattern PASSWORD_LOWERCASE_PATTERN =
            Pattern.compile("(?=.*[a-z])" + ".{0,}");

    public static final Pattern PASSWORD_NUMBER_PATTERN =
            Pattern.compile("(?=.*[0-9])" + ".{0,}");

    public static final Pattern PASSWORD_SPECIAL_CHARACTER_PATTERN =
            Pattern.compile("(?=.*[@#$%^&+=])" + ".{0,}");
}
