package com.example.camdavejakerob.classmanager;

import android.content.Intent;
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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Class for administration of UI to properly register new user and login existing users
*/
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    private EditText mSignInEmailField,
            mRegistrationEmailField,
            mSignInPasswordField,
            mRegistrationPasswordField,
            firstNameField,
            lastNameField;

    private String firstName, lastName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Bind form fields and buttons
        mSignInEmailField = findViewById(R.id.login_email_field);
        mRegistrationEmailField = findViewById(R.id.registration_email_field);
        mSignInPasswordField = findViewById(R.id.signIn_password);
        mRegistrationPasswordField = findViewById(R.id.registration_password);
        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);

        //consider making final
        TextView mVerificationLink = (TextView) findViewById(R.id.verification_link);
        TextView mSwitchToRegistration = (TextView) findViewById(R.id.switch_to_registration);
        TextView mPassReset = (TextView) findViewById(R.id.password_reset);
        Button mRegistrationButton = (Button) findViewById(R.id.register_button);
        Button mSigninButton = (Button) findViewById(R.id.sign_in_button);
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        Button mVerifyButton = (Button) findViewById(R.id.verify_button);

        /********************
        * LOGIN UI Buttons
         ********************/

        mSigninButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                String email = mSignInEmailField.getText().toString();
                String pass = mSignInPasswordField.getText().toString();

                if(pass.isEmpty()) {
                    mSignInPasswordField.setError("Required.");
                }
                else if(email.isEmpty() ) {
                    mSignInEmailField.setError("Required.");
                }
                else {
                    signIn(email, pass );
                }
            }
        });

        mPassReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = mSignInEmailField.getText().toString();

                if ( validateEmail(mSignInEmailField) ) {
                    FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                }
            }
        });

        mSwitchToRegistration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationUI();
            }
        });

        /*
        * Registration UI Buttons
        */

        mRegistrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(credentialValidation()) {
                    String email = mRegistrationEmailField.getText().toString();
                    String pass = mRegistrationPasswordField.getText().toString();

                    createAccount(email, pass);
                    VerificationUI();
                }
            }
        });

        /*
        * Verification UI Buttons
        */

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AuthUI.getInstance().signOut(LoginActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoginActivity.this,
                                        "You have been signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();

                                finish();
                            }
                        });
            }
        });

        mVerificationLink.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    sendEmailVerification();
                }
            }
        });

        mVerifyButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                mAuth.getCurrentUser().reload()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(mainIntent);
                                    return;
                                } else {
                                    Toast.makeText(LoginActivity.this, "Not Verified Yet",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Not Verified Yet",
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.getMessage());
                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If is logged
        if (currentUser != null) {

            //Is not verified, verify
            if(!currentUser.isEmailVerified()) {

                TextView finishSignInText = findViewById(R.id.finish_signIn_text);
                String nameString = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                finishSignInText.setText((getString(R.string.finish_signIn_text,
                        Html.fromHtml(nameString)  ) ) );

                VerificationUI();
            } else {
                finish();
            }

        }

        LoginUI();
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
        }
        // user is null
        else {
            LoginUI();
        }
    }

    public void handleFirebaseAuthResult(AuthResult result) {
        if(result != null) {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                LoginActivity.this.finish();
                return;
            } else {
                Toast.makeText(LoginActivity.this, "Not Verified Yet",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /******************************************/
    /*************UI Update********************/
    /******************************************/

     private void LoginUI() {
        findViewById(R.id.sign_in_form).setVisibility(View.VISIBLE);
        findViewById(R.id.registration_form).setVisibility(View.GONE);
        findViewById(R.id.verification_ui).setVisibility(View.GONE);
    }

    private void RegistrationUI() {
        findViewById(R.id.registration_form).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_in_form).setVisibility(View.GONE);
        findViewById(R.id.verification_ui).setVisibility(View.GONE);
    }

    private void VerificationUI() {

        findViewById(R.id.verification_ui).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_in_form).setVisibility(View.GONE);
        findViewById(R.id.registration_form).setVisibility(View.GONE);
    }

    /**************************************************/
    /*************Signin / Register********************/
    /**************************************************/

    /**
     * method is used for creating account
     * @param  email users email
     * @param  password users password
     * @return boolean true for valid false for invalid
     */
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

                            TextView finishSignInText = findViewById(R.id.finish_registration_text);
                            String nameString = "<b>" + firstName + " " + lastName + "</b> ";

                            finishSignInText.setText((getString(R.string.finish_registration_text,
                                    Html.fromHtml(nameString)  ) ) );

                            VerificationUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * method is used for creating account
     * @param  email users email
     * @param  password users password
     * @return boolean true for valid false for invalid
     */
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
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Not Verified Yet",
                                        Toast.LENGTH_SHORT).show();
                                VerificationUI();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Incorrect Username / Password Match",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification() {

        mAuth = FirebaseAuth.getInstance();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

        }

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

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

    /**
     * method is used for checking valid email id format
     * @param  editEmail is email field to be checkec
     * @return boolean true for valid false for invalid
     */
    private Boolean validateEmail(EditText editEmail) {

        boolean valid = true;

        String email = editEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Required.");
            valid = false;
        } else {
            editEmail.setError(null);
        }

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        // do if here to check this
        // return matcher.matches();

        return valid;
    }

    /**
     * method is used for checking valid email id format
     * @return boolean true for valid false for invalid
     */
    private boolean validateRegistration() {
        boolean valid = true;

        validateEmail(mRegistrationEmailField);

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

