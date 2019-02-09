package com.iQueues;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import data.GlobalUtils;
import data.Globals;

/**
 * The type Sign in process.
 */
public class SignInProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";

    private EditText loginEmail;
    private EditText loginPword;
    private ProgressDialog progressDialog;
    private String uid;
    private String name;
    private String email;
    private String pWord;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_process);

        Toolbar myToolbar = findViewById(R.id.sign_in_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        /**
         * Initialization of components
         */
        progressDialog = new ProgressDialog(SignInProcess.this);
        loginEmail = findViewById(R.id.login_email);
        loginPword = findViewById(R.id.login_Pword);

        TextView forgetPassword = findViewById(R.id.forget_password);
        Button logInBtn = findViewById(R.id.login_btn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v
             */
            @Override
            public void onClick(View v) {

                email = loginEmail.getText().toString().trim();
                pWord = loginPword.getText().toString().trim();

                /**
                 * Check email and password if entered correctly
                 */
                progressDialog.setMessage("מתחבר אנא המתן...");
                progressDialog.show();

                if (email.isEmpty()) {
                    loginEmail.setError("שדה האימייל ריק");
                    loginEmail.requestFocus();

                    progressDialog.dismiss();
                    return;
                } else if (pWord.isEmpty()) {
                    loginPword.setError("שדה הסיסמה ריק");
                    loginPword.requestFocus();

                    progressDialog.dismiss();
                    return;
                }

                /**
                 * The steps for signing in a user with a password are similar to the steps for creating a new account.
                 * When a user signs in to the app, passing the user's email address and password to signInWithEmailAndPassword:
                 * If sign-in succeeded, The sign-in is then approved and the customer is sent to the main activity.
                 */
                auth.signInWithEmailAndPassword(email, pWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isConnected()) {
                                Log.d(TAG, "signIn With Email:success");
                                Toast.makeText(SignInProcess.this, "התחברות בוצעה בהצלחה!", Toast.LENGTH_SHORT).show();

                                uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                name = auth.getCurrentUser().getDisplayName();
                                //save name and uid by SharedPreferences
                                GlobalUtils.setStringToLocalStorage(SignInProcess.this, Globals.UID_LOCAL_STORAGE_KEY, uid);
                                GlobalUtils.setStringToLocalStorage(SignInProcess.this, Globals.FULL_NAME_LOCAL_STORAGE_KEY, name);

                                goToMainScreen();
                            } else {
                                Toast.makeText(SignInProcess.this, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(SignInProcess.this, "אנא בדוק אימייל וסיסמא.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
        // make text clickable
        String text = "שכחתי סיסמא";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                changePassword();
            }
        };

        spannableString.setSpan(clickableSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        forgetPassword.setText(spannableString);
        forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());

        /**
         * sign Up Btn
         */
        Button signUpBtn = findViewById(R.id.sign_in_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent intent = new Intent(SignInProcess.this, SignUpProcess.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInProcess.this, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Insert to main activity automatically when the uid not null
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (GlobalUtils.getStringFromLocalStorage(this, Globals.UID_LOCAL_STORAGE_KEY) != null && isConnected()) {
            goToMainScreen();
        }
    }

    /**
     * change Password request.
     * send email by fire store to change the password.
     */
    private void changePassword() {

        email = loginEmail.getText().toString().trim();
        if (email.isEmpty()) {
            loginEmail.setError("שדה האימייל ריק");
            loginEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(SignInProcess.this, "האימייל נשלח.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * go to the main activity
     */
    private void goToMainScreen() {

        Intent intent = new Intent(SignInProcess.this, DriverMainActivity.class);
        startActivity(intent);
    }

    /**
     * @return is network Connected.
     */
    private boolean isConnected() {
        Context context = getApplicationContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}




