package edu.monash.iforme;

/**
 * Created by yashkhandha on 2/05/2018.
 */

public class User {

    //to store user first and last name
    public String firstName;
    public String lastName;
    // to store user date of birth
    public String dateOfBirth;
    //to store address of user
    public String address;
    //to store post code of user address
    public String postCode;
    //to store the mobile number
    public String mobile;
    //to store email address
    public String email;
    //to store user name
    public String userName;


    /**
     * Default constructor
     */
    public User() {
    }


    /**
     * Parametrized constructor
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param address
     * @param postCode
     * @param mobile
     * @param email
     * @param userName
     */
    public User(String firstName, String lastName, String dateOfBirth, String address, String postCode, String mobile, String email, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.postCode = postCode;
        this.mobile = mobile;
        this.email = email;
        this.userName = userName;
    }
}
