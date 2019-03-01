package edu.monash.iforme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mDateOfBirth;
    private EditText mAddress;
    private EditText mPostCode;
    private EditText mMobile;
    private EditText mEmail;
    private EditText mUserName;
    private EditText mPassword;
    Button register;
    ProgressBar progressBar;

    //to fetch the values entered by the user in the registration form
    String firstName;
    String lastName;
    String dateOfBirth;
    String address;
    String postCode;
    String mobile;
    String email;
    String userName;
    String password;

    //for formatting the date of birth
    Calendar mCurrentDate;
    int day, month, year;

    //Firebase auth reference
    private FirebaseAuth mAuth;

    //RegisterValidationHelper class object
    RegisterValidationHelper registerValidationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the activity_login.xml on start
        setContentView(R.layout.activity_register);
        //Toolbar reference
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set the title for the toolbar
        getSupportActionBar().setTitle("Register here");

        //Get reference for fields of registration form
        mFirstName = findViewById(R.id.firstname);
        mLastName = findViewById(R.id.lastName);
        mDateOfBirth = findViewById(R.id.dateofbirth);
        mAddress = findViewById(R.id.address);
        mPostCode = findViewById(R.id.postcode);
        mMobile = findViewById(R.id.mobile);
        mEmail = findViewById(R.id.emailId);
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        register = (Button)findViewById(R.id.registerButtonRegister);

        //Get reference for the progress bar i.e. ring
        progressBar = findViewById(R.id.register_progress);

        //Set current date to the fields to load the date picker with an initial date
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        //Get reference for logged in user
        mAuth = FirebaseAuth.getInstance();

        //Event listener for date of birth
        mDateOfBirth.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setDate();
            }
        });

        //Event listener for click of register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //instantiate object
        registerValidationHelper = new RegisterValidationHelper();
    }

    /**
     * Method to show the datepicker dialog box for selecting the date of birth
     */
    private void setDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //formatting the date to display and store in the database
                monthOfYear+=1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                //formatting the correct date value
                if(monthOfYear < 10){
                    formattedMonth = "0" + monthOfYear;
                }
                if(dayOfMonth < 10){
                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                mDateOfBirth.setText(year + "-" +  (formattedMonth) + "-" + formattedDayOfMonth );
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Method to attempt register based on user entered fields in registration form
     */
    private void registerUser() throws ParseException {

        //Set errors to null
        mFirstName.setError(null);
        mLastName.setError(null);
        mDateOfBirth.setError(null);
        mAddress.setError(null);
        mPostCode.setError(null);
        mMobile.setError(null);
        mEmail.setError(null);
        mUserName.setError(null);
        mPassword.setError(null);

        //fetch the values entered by the user in the registration form
        final String firstName;
        firstName = mFirstName.getText().toString();
        final String lastName;
        lastName = mLastName.getText().toString();
        final String dateOfBirth;
        dateOfBirth = mDateOfBirth.getText().toString();
        final String address;
        address = mAddress.getText().toString();
        final String postCode;
        postCode = mPostCode.getText().toString();
        final String mobile;
        mobile = mMobile.getText().toString();
        final String email;
        email = mEmail.getText().toString();
        final String userName;
        userName = mUserName.getText().toString();
        String password;
        password = mPassword.getText().toString();

        //check Validations for fields
        //to check if the first name is an empty string and show Error
        if(!registerValidationHelper.checkFirstNameNotNull(firstName) || !registerValidationHelper.checkFirstNameNotEmpty(firstName)){
            progressBar.setVisibility(View.GONE);
            mFirstName.requestFocus();
            mFirstName.setError(getString(R.string.error_first_name));
            return;
        }
        //to check if last name is an empty string and show Error
        else if(!registerValidationHelper.checkLastNameNotEmpty(lastName) || !registerValidationHelper.checkLastNameNotNull(lastName)){
            progressBar.setVisibility(View.GONE);
            mLastName.requestFocus();
            mLastName.setError(getString(R.string.error_surname));
            return;
        }
        //to check if date of birth is empty and show Error
        else if(!registerValidationHelper.checkDateOfBirthNotEmpty(dateOfBirth) || !registerValidationHelper.checkDateOfBirthNotNull(dateOfBirth)){
            progressBar.setVisibility(View.GONE);
            mDateOfBirth.requestFocus();
            mDateOfBirth.setError(getString(R.string.error_date_of_birth));
            return;
        }
        //to check if date of birthdate is invalid and show Error
        else if(! registerValidationHelper.checkDateOfBirthValid(dateOfBirth)){
            progressBar.setVisibility(View.GONE);
            mDateOfBirth.requestFocus();
            mDateOfBirth.setError(getString(R.string.error_date_of_birth_after));
            Toast.makeText(RegisterActivity.this,"Please enter date before today",Toast.LENGTH_LONG).show();
            return;
        }
        //to check if address is an empty string and show Error
        else if(!registerValidationHelper.checkAddressNotEmpty(address) || !registerValidationHelper.checkAddressNotNull(address)){
            progressBar.setVisibility(View.GONE);
            mAddress.requestFocus();
            mAddress.setError(getString(R.string.error_address));
            return;
        }
        //to check if the postcode is invalid and show Error
        else if(!registerValidationHelper.checkPostCodeValid(postCode)){
            progressBar.setVisibility(View.GONE);
            mPostCode.requestFocus();
            mPostCode.setError(getString(R.string.error_post_code_size));
            return;
        }
        //to check if the mobile number entered is empty
        else if(!registerValidationHelper.checkMobileNumberCodeValid(mobile)){
            progressBar.setVisibility(View.GONE);
            mMobile.requestFocus();
            mMobile.setError(getString(R.string.error_mobile_number_size));
            return;
        }
        //to check if email field is an empty string and show Error
        else if(!registerValidationHelper.checkEmailNotEmpty(email) || !registerValidationHelper.checkEmailNotNull(email)){
            progressBar.setVisibility(View.GONE);
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_email));
            return;
        }
        //to check if email is valid. Show error if invalid
        else if (!registerValidationHelper.checkEmailValid(email)) {
            progressBar.setVisibility(View.GONE);
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        //to check if the username field is an empty string. show Error if empty or null
        else if(!registerValidationHelper.checkUserNameNotEmpty(userName) || !registerValidationHelper.checkUserNameNotNull(userName)){
            progressBar.setVisibility(View.GONE);
            mUserName.requestFocus();
            mUserName.setError(getString(R.string.error_user_name));
            return;
        }
        //to check if password field is an empty string. show Error if empty or null
        else if(!registerValidationHelper.checkPasswordNotEmpty(password) || !registerValidationHelper.checkPasswordNotNull(password)){
            progressBar.setVisibility(View.GONE);
            mPassword.requestFocus();
            mPassword.setError(getString(R.string.error_password));
            return;
        }
        //to check password length and regex expression
        else if(!registerValidationHelper.checkPasswordValid(password)) {
            progressBar.setVisibility(View.GONE);
            mPassword.requestFocus();
            mPassword.setError(getString(R.string.error_invalid_password_regex));
            return;
        }

        //set the visibility of progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        //Firebase method to register a new suer and saving its details into firebase database Users node
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Registration success, update user object with details entered in registration form
                            User user = new User(firstName,lastName,dateOfBirth,address,postCode,mobile,email,userName);

                            //Get reference of Users node to save user details based on unique user id
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //if user creation is successful show success messsage and redirect to MainActivity.java class using Intent
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"You have been registered successfully",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                        startActivity(new Intent(RegisterActivity.this,WelcomeActivity.class));
                                    }
                                }
                            });
                        }
                        //If registration fails, display a message to the user.
                        else {

                            // User already registered with provided email, show appropriate message to user
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this,"You are already registered",
                                        Toast.LENGTH_LONG).show();

                            }
                            // Connection error or other failure. Show to user
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
