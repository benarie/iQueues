package com.iQueues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";

    private EditText email, pWord, phone, hatNum, fullName;
    private ProgressDialog progressDialog;
    Button signUpBtn;
    private String uFullName, uPhone, uHat, uCompany;
    private AutoCompleteTextView taxiCompaniesTv;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private CollectionReference userRef = database.collection("users");
    private DatabaseReference companiesRef = db.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_process);

        //initializing views
        email = findViewById(R.id.email);
        pWord = findViewById(R.id.pWord);
        fullName = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone_num);
        hatNum = findViewById(R.id.hat_num);

        /* GET  TAXI COMPAIES FROM FIREBASE */

        companiesRef.child("companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> companies = new ArrayList<>();

                for (DataSnapshot companiesSnapshot : dataSnapshot.getChildren()) {
                    String taxiCompanies = companiesSnapshot.child("taxiCompanies").getValue(String.class);
                    companies.add(taxiCompanies);
                }

                taxiCompaniesTv = findViewById(R.id.taxi_companies);
                ArrayAdapter<String> companiesAdapter = new ArrayAdapter<>(SignUpProcess.this, android.R.layout.simple_list_item_1, companies);
                companiesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                taxiCompaniesTv.setAdapter(companiesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });


        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uEmail = email.getText().toString().trim();
                String uPword = pWord.getText().toString().trim();

                if (uEmail.isEmpty() || uPword.isEmpty()) {//if the email or password are empty, you can't be registered
                    Toast.makeText(SignUpProcess.this, "email or password fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (uPword.length() < 6) {
                    Toast.makeText(SignUpProcess.this, "The password need to be over 6 digit", Toast.LENGTH_SHORT).show();

                }
                progressDialog = new ProgressDialog(SignUpProcess.this);
                progressDialog.setMessage("registering user please wait.");
                progressDialog.show();


                auth.createUserWithEmailAndPassword(uEmail, uPword).addOnCompleteListener(SignUpProcess.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checks if the request has been successfully sent
                        if (task.isSuccessful()) {

                            uFullName = fullName.getText().toString().trim();
                            uPhone = phone.getText().toString().trim();
                            uHat = hatNum.getText().toString().trim();
                            uCompany = taxiCompaniesTv.getText().toString().trim();

                            //creates an object which contains all above strings.

                            UserDetails userDetails = new UserDetails(uFullName, uPhone, uHat, uCompany);

                            userRef.document(auth.getCurrentUser().getUid())
                                    .set(userDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUpProcess.this, "user saved", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpProcess.this, "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });

                            // Sign up success, update UI with the signed-in user's information

                            updateUser();

                            Toast.makeText(SignUpProcess.this, "registration successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(SignUpProcess.this, DriverMainScreen.class);
                            startActivity(intent);

                        } else {
                            // If sign up fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpProcess.this, "registration failed,please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    private void updateUser() {

        final FirebaseUser user = auth.getCurrentUser();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(uFullName)
                .build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated." + user.getDisplayName());
                        }
                    }
                });
    }


}
