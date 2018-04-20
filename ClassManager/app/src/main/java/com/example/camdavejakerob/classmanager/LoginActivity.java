package com.example.camdavejakerob.classmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "LoginActivity";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText firstNameField;
    private EditText lastNameField;

    private String firstName, lastName;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        final Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        final Button mResendVerificationButton = (Button) findViewById(R.id.resend_verification_link);
        final Button mFinishButton = (Button) findViewById(R.id.finish_button);

        mResendVerificationButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sendEmailVerification();
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                //see if user is already in DB
                /*mAuth.fetchProvidersForEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if(task.isSuccessful()){
                            ///////// getProviders().size() will return size 1. if email ID is available.
                            task.getResult().getProviders().size();
                        }
                    }
                });
                */

                mEmailField = findViewById(R.id.field_email);
                mPasswordField = findViewById(R.id.field_password);

                signIn(mEmailField.getText().toString()
                        , mPasswordField.getText().toString() );
            }
        });

        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailField = findViewById(R.id.field_email);
                mPasswordField = findViewById(R.id.field_password);

                String email = mEmailField.getText().toString();
                String pass = mPasswordField.getText().toString();

                createAccount(email
                        , pass );
            }
        });

        mFinishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.getCurrentUser().reload();

                if ( mAuth.getCurrentUser().isEmailVerified() ) {
                    if (credentialValidation()) {

                        FirebaseUser user = mAuth.getCurrentUser();
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

                        //finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Not Verified Yet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean credentialValidation() {
        boolean valid = true;

        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);

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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {

        //hideProgressDialog();

        if (user != null) {
            //mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
            //        user.getEmail(), user.isEmailVerified()));

            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_field).setVisibility(View.GONE);

            findViewById(R.id.field_password).setVisibility(View.VISIBLE);

            findViewById(R.id.credential_fields).setVisibility(View.VISIBLE);

            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.resend_verification_link).setEnabled(true);
            findViewById(R.id.finish_button).setEnabled(true);

            //finish();

        } else {
            //mStatusTextView.setText("Signed out");
            //mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            sendEmailVerification();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("Authentication failed.");
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.finish_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.finish_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else if ( password.length() < 6 ) {
            mPasswordField.setError("Must be greater than 6 Characters.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    /*Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
        // This activity is NOT part of this app's task, so create a new task
        // when navigating up, with a synthesized back stack.
        TaskStackBuilder.create(this)
                // Add all of this activity's parents to the back stack
                .addNextIntentWithParentStack(upIntent)
                // Navigate up to the closest parent
                .startActivities();
    } else {
        // This activity is part of this app's task, so simply
        // navigate up to the logical parent activity.
        NavUtils.navigateUpTo(this, upIntent);
    }
                return true;
*/
}

