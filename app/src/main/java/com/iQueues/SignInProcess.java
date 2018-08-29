package com.iQueues;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import data.GlobalUtils;
import data.Globals;

public class SignInProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";

    private TextView loginEmail;
    private TextView loginPword;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    private String uid;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference userRef = database.collection("users");
    private CollectionReference orderRef = database.collection("orders");

    UserDetails userDetails = UserDetails.getInstance();
    OrdersQueue queue = OrdersQueue.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_process);

        progressBar = findViewById(R.id.progress_bar_0);
        progressDialog = new ProgressDialog(this);

        if (GlobalUtils.getStringFromLocalStorage(this, Globals.UID_LOCAL_STORAGE_KEY) != null) {

            uid = GlobalUtils.getStringFromLocalStorage(this, Globals.UID_LOCAL_STORAGE_KEY);
            pullDataOfUserFromFireStore(uid);

            goToMainScreen();

            return;
        }

        loginEmail = findViewById(R.id.login_email);
        loginPword = findViewById(R.id.login_Pword);


        Button logInBtn = findViewById(R.id.login_btn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String pWord = loginPword.getText().toString().trim();


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

                            Log.d(TAG, "signIn With Email:success");

                            Toast.makeText(SignInProcess.this, "signIn successful", Toast.LENGTH_SHORT).show();

                            uid = auth.getCurrentUser().getUid();

                            //pull data frome fireStore
                            userRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    userDetails = snapshot.toObject(UserDetails.class);

                                    GlobalUtils.setStringToLocalStorage(SignInProcess.this, Globals.UID_LOCAL_STORAGE_KEY, uid);
                                    GlobalUtils.setStringToLocalStorage(SignInProcess.this, Globals.FULL_NAME_LOCAL_STORAGE_KEY, auth.getCurrentUser().getDisplayName());

                                    goToMainScreen();
                                }
                            });
                        } else {

                            Toast.makeText(SignInProcess.this, "email or password are incorrect,please check your entries.", Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });


        Button signInBtn = findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInProcess.this, SignUpProcess.class);
                startActivity(intent);
            }
        });

    }

    private void goToMainScreen() {
        Intent intent = new Intent(SignInProcess.this, DriverMainScreen.class);
        startActivity(intent);
    }

    private void pullDataOfUserFromFireStore(final String uid) {

        progressBar.setVisibility(View.VISIBLE);

        userRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                userDetails = snapshot.toObject(UserDetails.class);
            }
        });

        progressBar.setVisibility(View.GONE);
    }


}
