package com.example.chessproject.UserUtils;

public class VerificationUtils {

    public static String UserNameVerification(String username){
        String errors = "";
        if (username.length() < 6){
            errors += "User name must be 6 digit at least";
        }
        return errors;
    }

    public static String passwordVerification(String password){
        String errors = "";

        if (password.length() < 8){
            errors += "Password must be 8 digit at least";
        }

        for (int i = 0; i < password.length(); i++){
            if (!((password.charAt(i) >= 'a' && password.charAt(i) <= 'z') || (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') || (Character.isDigit(password.charAt(i))) )){
                errors += "\nPassword must contain only english letters";
                break;
            }
        }

        boolean oneDigit = false;
        for (int i =0; i < password.length(); i++){
            if (Character.isDigit(password.charAt(i)))
                oneDigit = true;
        }
        if (oneDigit = false)
            errors += "\nPassword must contain one digit at least";

        return errors;
    }
}
