package com.example.camdavejakerob.classmanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;


public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    private EditText mEmailField;
    private EditText mSignInPasswordField;
    private EditText mRegistrationPasswordField;
    private EditText firstNameField;
    private EditText lastNameField;

    private String firstName, lastName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailField = findViewById(R.id.email_field);
        mSignInPasswordField = findViewById(R.id.signIn_password);
        mRegistrationPasswordField = findViewById(R.id.registration_password);
        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);

        final Button mEmailEntry = (Button) findViewById(R.id.email_entry_button);
        final Button mResendVerificationButton = (Button) findViewById(R.id.resend_verification_link);
        final Button mFinishButton = (Button) findViewById(R.id.finish_button);
        final Button mSigninButton = (Button) findViewById(R.id.sign_in_button);
        final Button mRegisterButton = (Button) findViewById(R.id.register_button);

        mResendVerificationButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sendEmailVerification();
            }
        });

        mEmailEntry.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if( validateEmail() ) {
                   updateUI();
                }
            }
        });

        mSigninButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                String email = mEmailField.getText().toString();
                String pass = mSignInPasswordField.getText().toString();

                if(validateEmail()) {
                    signIn(email, pass );
                }

                mAuth = FirebaseAuth.getInstance();

                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    mAuth.getCurrentUser().reload();
                    if ( mAuth.getCurrentUser().isEmailVerified() ) {
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Not Verified Yet",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mFinishButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                mAuth.getCurrentUser().reload();

                if ( mAuth.getCurrentUser().isEmailVerified() ) {
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Not Verified Yet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailField.getText().toString();
                String pass = mRegistrationPasswordField.getText().toString();

                    if (credentialValidation()) {
                        createAccount(email, pass);
                    }
                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUIOnStart(currentUser);
    }

    @Override
    public void onBackPressed()
    {
        final FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null ) {

                if ( mAuth.getCurrentUser().isEmailVerified() ) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(LoginActivity.this, "Email Not Verified",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login Incomplete",
                        Toast.LENGTH_SHORT).show();
            }
    }

    /******************************************/
    /*************UI Update********************/
    /******************************************/

    //This is after inputting an email
    private void updateUI() {

        String email = mEmailField.getText().toString();

        //see if email is already registered
        mAuth.fetchProvidersForEmail( email ).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if(task.isSuccessful()){
                    ///////// getProviders().size() will return size 1. if email ID is available.
                    int num = task.getResult().getProviders().size();

                    //signIn
                    if( (num > 0) ) {
                        findViewById(R.id.sign_in_form).setVisibility(View.VISIBLE);
                        findViewById(R.id.email_form).setVisibility(View.GONE);
                        findViewById(R.id.sign_in_button).setEnabled(true);
                        findViewById(R.id.send_verification_link).setEnabled(true);
                        findViewById(R.id.sign_In_buttons).setVisibility(View.VISIBLE);
                    }
                    //Register
                    else {
                        findViewById(R.id.email_form).setVisibility(View.GONE);
                        findViewById(R.id.registration_form).setVisibility(View.VISIBLE);
                        findViewById(R.id.registration_buttons).setVisibility(View.VISIBLE);
                        findViewById(R.id.register_button).setEnabled(true);
                        findViewById(R.id.resend_verification_link).setEnabled(true);
                    }
                }
            }
        });
    }

    private void updateRegistrationUi() {

        findViewById(R.id.finish_registration_text).setVisibility(View.VISIBLE);

        firstName = firstNameField.getText().toString();
        lastName = lastNameField.getText().toString();

        TextView finishSignInText = findViewById(R.id.finish_registration_text);
        String nameString = "<b>" + firstName + " " + lastName + "</b> ";

        finishSignInText.setText((getString(R.string.finish_registration_text,
                Html.fromHtml(nameString)  ) ) );

        findViewById(R.id.registration_form).setVisibility(View.GONE);
        findViewById(R.id.registration_buttons).setVisibility(View.GONE);
        findViewById(R.id.finish_button_form).setVisibility(View.VISIBLE);
        findViewById(R.id.finish_button).setEnabled(true);
    }

    //Is onStart and if signIn Fails
    private void updateUIOnStart(FirebaseUser user) {

        //Already a user, inflate password and sign in button
        if (user != null) {
            findViewById(R.id.registration_form).setVisibility(View.GONE);
            findViewById(R.id.registration_buttons).setVisibility(View.GONE);
            findViewById(R.id.sign_in_form).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_In_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.send_verification_link).setVisibility(View.VISIBLE);
            findViewById(R.id.email_form).setVisibility(View.GONE);
            findViewById(R.id.finish_button_form).setVisibility(View.GONE);

            findViewById(R.id.send_verification_link).setEnabled(true);
            findViewById(R.id.sign_in_button).setEnabled(true);
            //Not a user, inflate email form
        } else {
            findViewById(R.id.email_form).setVisibility(View.VISIBLE);
        }
    }

    //Is onStart and if signIn Fails
    private void updateUI(FirebaseUser user) {

        //Already a user, inflate password and sign in button
        if (user != null) {
            findViewById(R.id.registration_form).setVisibility(View.GONE);
            findViewById(R.id.registration_buttons).setVisibility(View.GONE);
            findViewById(R.id.sign_in_form).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_In_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.send_verification_link).setVisibility(View.VISIBLE);
            findViewById(R.id.email_form).setVisibility(View.GONE);
            findViewById(R.id.finish_button_form).setVisibility(View.GONE);

            findViewById(R.id.send_verification_link).setEnabled(true);
            findViewById(R.id.sign_in_button).setEnabled(true);

            //Not a user, inflate password, firstname, lastname, and register button
        } else {

            findViewById(R.id.registration_form).setVisibility(View.VISIBLE);
            findViewById(R.id.registration_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_form).setVisibility(View.GONE);
            findViewById(R.id.email_form).setVisibility(View.GONE);
            findViewById(R.id.finish_button_form).setVisibility(View.GONE);

            findViewById(R.id.register_button).setEnabled(true);
            findViewById(R.id.resend_verification_link).setEnabled(true);
        }
    }

    /**************************************************/
    /*************Signin / Register********************/
    /**************************************************/

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateRegistration()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            firstName = firstNameField.getText().toString();
                            lastName = lastNameField.getText().toString();

                            String credentials = firstName + " " + lastName;

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(credentials).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            DatabaseHelper databaseHelper = new DatabaseHelper();
                            databaseHelper.addUser(LoginActivity.this, user);

                            sendEmailVerification();
                            updateRegistrationUi();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if ( mAuth.getCurrentUser().isEmailVerified() ) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Not Verified Yet",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.finish_button).setEnabled(false);

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable button
                        findViewById(R.id.finish_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**************************************************/
    /*************Validations**************************/
    /**************************************************/

    private Boolean credentialValidation() {
        boolean valid = true;

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            firstNameField.setError("Required.");
            valid = false;
        } else if ( !isAlpha(firstName) ) {
            firstNameField.setError("Only Letters.");
            valid = false;
        } else {
            firstNameField.setError(null);
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameField.setError("Required.");
            valid = false;
        } else if ( !isAlpha(lastName) ) {
            lastNameField.setError("Only Letters.");
            valid = false;
        }  else {
            lastNameField.setError(null);
        }

        return valid;
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private Boolean validateEmail() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        return valid;
    }

    private boolean validateRegistration() {
        boolean valid = true;

        String password = mRegistrationPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mRegistrationPasswordField.setError("Required.");
            valid = false;
        } else if ( password.length() < 6 ) {
            mRegistrationPasswordField.setError("Must be greater than 6 Characters.");
            valid = false;
        } else {
            mRegistrationPasswordField.setError(null);
        }

        return valid;
    }
}

