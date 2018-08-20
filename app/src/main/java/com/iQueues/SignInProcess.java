package com.iQueues;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignInProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";

    private TextView loginEmail;
    private TextView loginPword;
    private ProgressDialog progressDialog;


    private FirebaseAuth auth = FirebaseAuth.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_process);

        loginEmail = findViewById(R.id.login_email);
        loginPword = findViewById(R.id.login_Pword);


        Button logInBtn = findViewById(R.id.login_btn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String pWord = loginPword.getText().toString().trim();

                progressDialog = new ProgressDialog(SignInProcess.this);
                progressDialog.setMessage("login");
                progressDialog.show();

                if (email.isEmpty() || pWord.isEmpty()) {

                    Toast.makeText(SignInProcess.this, "email or password fields are empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                auth.signInWithEmailAndPassword(email, pWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");

                            Toast.makeText(SignInProcess.this, "signIn successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(SignInProcess.this, DriverMainScreen.class);
                            startActivity(intent);

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(SignInProcess.this, "email or password are incorrect,please check your entries.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });


        Button signInBtn = findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInProcess.this, SignUpProcess.class);
                startActivity(intent);
            }
        });


        /*authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is already sign in.

                    boolean emailVerified = user.isEmailVerified();

                }
            }

        };*/
    }
}
