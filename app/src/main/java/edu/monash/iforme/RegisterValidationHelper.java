package edu.monash.iforme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ykha0002 on 9/6/18.
 */

public class RegisterValidationHelper {

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
     * method to check if first name is not empty
     * @param firstName
     * @return true if first name is not empty
     */
    public boolean checkFirstNameNotEmpty(String firstName){
        if(firstName.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if first name is not null
     * @param firstName
     * @return true if first name is not null
     */
    public boolean checkFirstNameNotNull(String firstName){
        if(firstName != null){
            return true;
        }
        return false;
    }

    /**
     * method to check if last name is not empty
     * @param lastName
     * @return true if last name is not empty
     */
    public boolean checkLastNameNotEmpty(String lastName){
        if(lastName.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if last name is not null
     * @param lastName
     * @return true if last name is not null
     */
    public boolean checkLastNameNotNull(String lastName){
        if(lastName != null){
            return true;
        }
        return false;
    }

    /**
     * method to check if date of birth is not empty
     * @param dateOfBirth
     * @return true if date of birth is not empty
     */
    public boolean checkDateOfBirthNotEmpty(String dateOfBirth){
        if(dateOfBirth.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if date of birth is not null
     * @param dateOfBirth
     * @return true if date of birth is not null
     */
    public boolean checkDateOfBirthNotNull(String dateOfBirth){
        if(dateOfBirth != null){
            return true;
        }
        return false;
    }

    /**
     * method to check if date of birth is valid i.e. before todays date
     * @param dateOfBirth
     * @return true if date of birth is not valid
     */
    public boolean checkDateOfBirthValid(String dateOfBirth) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = dateFormat.parse(dateOfBirth);

        String d = dateFormat.format(new Date());
        Date currentDate = dateFormat.parse(d);

        if(dob.compareTo(currentDate) <=0 ){
            return true;
        }
        return false;
    }

    /**
     * method to check if address is not empty
     * @param address
     * @return true if address is not empty
     */
    public boolean checkAddressNotEmpty(String address){
        if(address.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if address is not null
     * @param address
     * @return true if address is not null
     */
    public boolean checkAddressNotNull(String address){
        if(address != null){
            return true;
        }
        return false;
    }

    /**
     * method to check if post code is valid i.e. 4 digits and no characters
     * @param postCode
     * @return true if post code is valid
     */
    public boolean checkPostCodeValid(String postCode){
        if(postCode.length() == 4 && postCode.matches(".*\\d+.*")){
            return true;
        }
        return false;
    }

    /**
     * method to check if mobile number is valid i.e. 10 digits and no characters
     * @param mobileNumber
     * @return true if mobile number is valid
     */
    public boolean checkMobileNumberCodeValid(String mobileNumber){
        if(mobileNumber.length() == 10 && mobileNumber.matches(".*\\d+.*")){
            return true;
        }
        return false;
    }

    /**
     * method to check if user name is not empty
     * @param userName
     * @return true if user name is not empty
     */
    public boolean checkUserNameNotEmpty(String userName){
        if(userName.length()>0){
            return true;
        }
        return false;
    }

    /**
     * method to check if user name is not null
     * @param userName
     * @return true if user name is not null
     */
    public boolean checkUserNameNotNull(String userName){
        if(userName != null){
            return true;
        }
        return false;
    }


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
     * method to check password is valid if consists of minimum 6 characters, one uppercase, one lowercase, one digit and one of (@#$%^&+=) atleast
     * @param password
     * @return true if password is valid
     */
    public boolean checkPasswordValid(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }





}
