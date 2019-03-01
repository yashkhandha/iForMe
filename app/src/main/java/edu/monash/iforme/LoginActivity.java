package edu.monash.iforme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mNewUser;
    Button mEmailSignInButton;

    //Firebase auth reference
    private FirebaseAuth mAuth;

    //LoginValidationHelper class instance to check validations of form
    LoginValidationHelper validation;

    /**
     * onCreate method loaded at first
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the activity_login.xml on start
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        //Get reference for sign in button
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        //Get reference for the register button
        mNewUser = (Button)findViewById(R.id.registerOption);
        //Get reference for login form and progress
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //Get reference for logged in user
        mAuth = FirebaseAuth.getInstance();
        //Event listener for sign in button
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Attempt to login with email and password
                attemptLogin();
            }
        });
        //Event listener for register button
        mNewUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //start register activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        //instantiate LoginValidationHelper class object
        validation = new LoginValidationHelper();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        //Start the progress bar (ring)
        showProgress(true);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check if email field is not empty using LoginValidationHelper Helper class
        if (!validation.checkEmailNotEmpty(email)) {
            //Stop the progress bar (ring)
            showProgress(false);
            //Focus on email field
            mEmailView.requestFocus();
            //Animation for vibration effect on email field
            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(mEmailView);
            //set error by defining them in strings.xml
            mEmailView.setError(getString(R.string.error_field_required));
            return;
        }

        //Check if email is valid using LoginValidationHelper Helper class
        else if (!validation.checkEmailValid(email)) {
            //Stop the progress bar (ring)
            showProgress(false);
            //focus on email field
            mEmailView.requestFocus();
            //Animation for vibration effect on email field
            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(mEmailView);
            //set error by defining them in strings.xml
            mEmailView.setError(getString(R.string.error_invalid_email));
            return;
        }

        //Check if password field is not empty using LoginValidationHelper Helper class
        else if (!validation.checkPasswordNotEmpty(password)) {
            //Stop the progress bar (ring)
            showProgress(false);
            //focus on password field
            mPasswordView.requestFocus();
            //Animation for vibration effect on password field
            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(findViewById(R.id.password));
            //set error by defining them in strings.xml
            mPasswordView.setError(getString(R.string.error_field_required));
            return;
        }

        // Check for a valid password, if the user entered one. using LoginValidationHelper Helper class
        else if (!validation.checkPasswordValid(password)) {
            //Stop the progress bar (ring)
            showProgress(false);
            //focus on password field
            mPasswordView.requestFocus();
            //Animation for vibration effect on password field
            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(findViewById(R.id.password));
            //set error by defining them in strings.xml
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the login credentials match with the database. i.e. login successfull
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            //Stop the progress bar i.e. ring
                            showProgress(false);
                            //fetch the details of current logged in user
                            FirebaseUser user = mAuth.getCurrentUser();
                            //finish this activity
                            finish();
                            //Define intent for starting the MainActivity.java class
                            Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //Start the MainActivity.java class
                            startActivity(intent);
                        }
                        //login credentials do not macth with the database. i.e. login failure
                        else {
                            //Stop the progress bar i.e. ring
                            showProgress(false);
                            // If sign in fails, display a message to the user.
                            Toast toast = Toast.makeText(LoginActivity.this,"Please check your credentials",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            //Animation for vibration effect on email and password fields
                            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(findViewById(R.id.password));
                            YoYo.with(Techniques.Tada).duration(400).repeat(1).playOn(mEmailView);
                            //Set error on email and password fields
                            mEmailView.setError("Please check your credentials");
                            mPasswordView.setError("Please check your credentials");
                        }
                    }
                });
        }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * method to start the main activity if the authorization is valid for the logged in user
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

    }
    }


