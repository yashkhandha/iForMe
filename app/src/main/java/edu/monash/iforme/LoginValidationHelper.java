package edu.monash.iforme;

import android.text.TextUtils;
import android.util.Patterns;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ykha0002 on 9/6/18.
 */

public class LoginValidationHelper {


    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * method to check if email is not empty
     * @param email
     * @return true if email is not empty
     */
    public boolean checkEmailNotEmpty(String email){
        if(email.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if email is not null
     * @param email
     * @return true if email is not null
     */
    public boolean checkEmailNotNull(String email){
        if(email != null){
            return true;
        }
        return false;
    }

    /**
     * method to check if email is valid
     * @param email
     * @return true if email is valid
     */
    public boolean checkEmailValid(String email) {

        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * method to check if password is not empty
     * @param password
     * @return true if password is not empty
     */
    public boolean checkPasswordNotEmpty(String password){
        if(password.length()>0){
            return true;
        }
        else return false;
    }

    /**
     * method to check if password is not null
     * @param password
     * @return true if password is not null
     */
    public boolean checkPasswordNotNull(String password){
        if(password != null){
            return true;
        }
        else return false;
    }

    /**
     * method to check password is valid if consists of minimum 6 characters
     * @param password
     * @return true if password is valid
     */
    public boolean checkPasswordValid(String password) {
        return password.length() >= 6;
    }

}
