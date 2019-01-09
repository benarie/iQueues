package com.iQueues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import data.GlobalUtils;
import data.Globals;


public class SignUpProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";
    private String PHONE_NUMBER_TAG = "PHONE_NUMBER_TAG";


    private EditText email, pWord, phone, hatNum, fullName;
    private TextView afterVerifyingTv;
    private ProgressDialog progressDialog;
    Button signUpBtn;
    Button verificationBtn;
    private String uFullName, uPhone, uHat, uCompany, uid, position;
    private String codeSent;
    private AutoCompleteTextView taxiCompaniesTv;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

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
        taxiCompaniesTv = findViewById(R.id.taxi_companies);
        hatNum = findViewById(R.id.hat_num);
        progressDialog = new ProgressDialog(SignUpProcess.this);
        afterVerifyingTv = findViewById(R.id.after_verifying_text_view);

        getTaxiCompaniesList(); //  GET  TAXI COMPANIES FROM FIRE BASE

        signUpBtn = findViewById(R.id.sign_up_btn);
        verificationBtn = findViewById(R.id.verification_btn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("registering user please wait...");
                progressDialog.show();

                String uEmail = email.getText().toString().trim();
                String uPword = pWord.getText().toString().trim();

                uFullName = fullName.getText().toString().trim();
                uPhone = phone.getText().toString().trim();
                uHat = hatNum.getText().toString().trim();
                uCompany = taxiCompaniesTv.getText().toString().trim();

                if (auth.getCurrentUser() != null) {
                    uid = auth.getCurrentUser().getUid();
                }
                if (uFullName.isEmpty()) {
                    fullName.setError("Email fields are empty");
                    fullName.requestFocus();
                    return;
                } else if (uEmail.isEmpty()) {//if the email or password are empty, you can't be registered
                    email.setError("Email fields is empty");
                    email.requestFocus();
                    return;
                } else if (uPword.isEmpty()) {
                    pWord.setError("Password fields is empty");
                    pWord.requestFocus();
                    return;
                } else if (uPword.length() < 6) {
                    pWord.setError("The password need to be over 6 digit");
                    pWord.requestFocus();
                    return;
                } else if (uPhone.isEmpty()) {
                    phone.setError("Phone number is required");
                    phone.requestFocus();
                    return;
                } else if (uHat.isEmpty()) {
                    hatNum.setError("Hat number is empty");
                    hatNum.requestFocus();
                    return;
                }


                auth.createUserWithEmailAndPassword(uEmail, uPword).addOnCompleteListener(SignUpProcess.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checks if the request has been successfully sent
                        if (task.isSuccessful()) {

                            // Sign up success, update UI with the signed-in user's information

                            updateUser(user);
//                            sendVerificationEmail();

                            pushUserDetailsToFireStore();

                            Intent intent = new Intent(SignUpProcess.this, VerifyCode.class);
                            intent.putExtra("PHONE_NUMBER_TAG", uPhone);
                            startActivity(intent);


                        } else {
                            // If sign up fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpProcess.this, "registration failed,please try again The email or password is incorrect", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpProcess.this, "The email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//        waitForVerification();

    }


    private void getTaxiCompaniesList() {

        companiesRef.child("companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> companies = new ArrayList<>();

                for (DataSnapshot companiesSnapshot : dataSnapshot.getChildren()) {
                    String taxiCompanies = companiesSnapshot.child("taxiCompanies").getValue(String.class);
                    companies.add(taxiCompanies);
                }


                ArrayAdapter<String> companiesAdapter = new ArrayAdapter<>(SignUpProcess.this, android.R.layout.simple_list_item_1, companies);
                companiesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                taxiCompaniesTv.setAdapter(companiesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

//    private void sendVerificationEmail() {
//
//        if (user != null) {
//            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(SignUpProcess.this, "Verification email sent to" + user.getEmail(), Toast.LENGTH_SHORT).show();
//
//                        signUpBtn.setVisibility(View.GONE);
//                        afterVerifyingTv.setVisibility(View.VISIBLE);
//                        verificationBtn.setVisibility(View.VISIBLE);
//
//                        progressDialog.dismiss();
//
//                    } else {
//                        Log.e(TAG, "sendEmailVerification", task.getException());
//                        Toast.makeText(SignUpProcess.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
//        }
//    }

//    private void waitForVerification() {
//        verificationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                user.reload();
//                while (user.isEmailVerified()) {
//
//                    progressDialog.setMessage("Updating details Please wait...");
//                    progressDialog.show();
//
//                    if (user.isEmailVerified()) {
//
//                        verificationBtn.setClickable(true);
//                        Toast.makeText(SignUpProcess.this, "Sign up successful", Toast.LENGTH_SHORT).show();
//
////                        pushUserDetailsToFireStore();
//
//                        Intent intent = new Intent(SignUpProcess.this, DriverMainActivity.class);
//                        startActivity(intent);
//
//                        progressDialog.dismiss();
//
//                        break;
//                    }
//                }
//            }
//        });
//
//    }

    private void updateUser(final FirebaseUser user) {

        //save name and uid by SharedPreferences
        GlobalUtils.setStringToLocalStorage(SignUpProcess.this, Globals.UID_LOCAL_STORAGE_KEY, uid);
        GlobalUtils.setStringToLocalStorage(SignUpProcess.this, Globals.FULL_NAME_LOCAL_STORAGE_KEY, uFullName);

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(uFullName)
                .build();
        user.updateProfile(profileUpdate);
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User profile updated." + user.getDisplayName());
//                        }
//                    }
//                });
    }

    private void pushUserDetailsToFireStore() {

        //creates an object which contains all above strings.
        UserDetails userDetails = UserDetails.getInstance();
        userDetails.setParams(uid, uPhone, uHat, uCompany, position);

        userRef.document(uid)
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
    }

}
