package com.iQueues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

import data.GlobalUtils;
import data.Globals;


/**
 * The type Sign up process.
 */
public class SignUpProcess extends AppCompatActivity {

    private String TAG = "SignUpProcess";

    private EditText email, pWord, phone, hatNum, fullName;
    private ProgressDialog progressDialog;
    /**
     * The Sign up btn.
     */
    Button signUpBtn;

    private String uFullName;
    private String uPhone;
    private String uHat;
    private String uCompany;
    private String uid;
    private AutoCompleteTextView taxiCompaniesTv;

    /**
     * The Auth.
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    /**
     * The User.
     */
    FirebaseUser user = auth.getCurrentUser();


    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private CollectionReference userRef = database.collection("users");
    private DatabaseReference companiesRef = db.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_process);

        Toolbar myToolbar = findViewById(R.id.sign_up_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        //initializing views
        email = findViewById(R.id.email);
        pWord = findViewById(R.id.pWord);
        fullName = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone_num);
        hatNum = findViewById(R.id.hat_num);
        progressDialog = new ProgressDialog(SignUpProcess.this);


        getTaxiCompaniesList(); //  GET  TAXI COMPANIES FROM FIRE BASE

        signUpBtn = findViewById(R.id.sign_up_btn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("אנא המתן!");
                progressDialog.show();

                final String uEmail = email.getText().toString().trim();
                String uPword = pWord.getText().toString().trim();

                uFullName = fullName.getText().toString().trim();
                uPhone = phone.getText().toString().trim();
                uHat = hatNum.getText().toString().trim();
                uCompany = taxiCompaniesTv.getText().toString().trim();
                /**
                 * Check name,email,password,phone and hat number if entered correctly
                 */
                if (uFullName.isEmpty()) {
                    fullName.setError("שדה השם ריק");
                    fullName.requestFocus();
                    progressDialog.cancel();
                    return;
                } else if (uEmail.isEmpty()) {//if the email or password are empty, you can't be registered
                    email.setError("שדה האימייל ריק");
                    email.requestFocus();
                    progressDialog.cancel();
                    return;
                } else if (uPword.isEmpty()) {
                    pWord.setError("שדה הסיסמה ריק");
                    pWord.requestFocus();
                    progressDialog.cancel();
                    return;
                } else if (uPword.length() < 6) {
                    pWord.setError("הסיסמה צריכה להיות בעלת 6 תווים או יותר");
                    pWord.requestFocus();
                    progressDialog.cancel();
                    return;
                } else if (uPhone.length() < 10) {
                    pWord.setError("מספר הטלפון חייב להכיל 10 תווים");
                    pWord.requestFocus();
                    progressDialog.cancel();
                } else if (uHat.isEmpty()) {
                    hatNum.setError("שדה הכובע ריק");
                    hatNum.requestFocus();
                    progressDialog.cancel();
                    return;
                }


                auth.createUserWithEmailAndPassword(uEmail, uPword).addOnCompleteListener(SignUpProcess.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checks if the request has been successfully sent
                        if (task.isSuccessful()) {

                            // Sign up success, update UI with the signed-in user's information
                            if (uid != null) uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                            //save name and uid by SharedPreferences
                            GlobalUtils.setStringToLocalStorage(SignUpProcess.this, Globals.UID_LOCAL_STORAGE_KEY, uid);
                            GlobalUtils.setStringToLocalStorage(SignUpProcess.this, Globals.FULL_NAME_LOCAL_STORAGE_KEY, uFullName);

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                            updateProfile(user);

                            pushUserDetailsToFireStore();

                            sendVerificationEmail();

                        } else {
                            // If sign up fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpProcess.this, "ההרשמה נכשלה, נסה שנית", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    /**
     * update the full name of user in FirebaseUser
     *
     * @param user  FirebaseUser user.
     */
    private void updateProfile(final FirebaseUser user) {


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

    /**
     * get Taxi Companies List from fire base
     */
    private void getTaxiCompaniesList() {

        companiesRef.child("companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /**
     * send verification email.
     */
    private void sendVerificationEmail() {

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpProcess.this, "המייל נשלח ל " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpProcess.this, VerifyEmailActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();

                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(SignUpProcess.this, "נכשל נסיון האימות.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    /**
     * push User Details ToF ireStore
     */
    private void pushUserDetailsToFireStore() {

        String attribute = Globals.ATTRIBUTE;

        //creates an object which contains all above strings.
        UserDetails userDetails = UserDetails.getInstance();
        userDetails.setParams(uid, uPhone, uHat, uCompany, uFullName, attribute);

        userRef.document(uid)
                .set(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "user saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString() + "user not save");
                    }
                });
    }
}
