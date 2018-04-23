package com.iqueues;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpProcess extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView email,pWord,phone,hatNum,fullName;
    private ProgressDialog progressDialog;
    private Spinner companies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_process);
        //initializing views
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        pWord = findViewById(R.id.pWord);
        fullName = findViewById(R.id.name);
        phone = findViewById(R.id.phoneNum);
        hatNum = findViewById(R.id.hatNum);

        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        companies = findViewById(R.id.taxiCompanies);

    }

    public void signUser(View v)
    {
        final String uEmail = email.getText().toString().trim();
        String uPword = pWord.getText().toString().trim();
        if(TextUtils.isEmpty(uEmail)|| TextUtils.isEmpty(uPword))
        {//if the email or password are empty, you can't be registered
            Toast.makeText(this,"email or password fields are empty",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("registering user please wait.");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(uEmail,uPword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checks if the request has been successfully sent
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpProcess.this,"registration successful",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            addUser();
                            
                        } else {
                            Toast.makeText(SignUpProcess.this,"registration failed,please try again",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

   public void addUser(){
       //gets current registered user object.
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String uId= firebaseUser.getUid();
            String uName= fullName.getText().toString().trim();
            String uPhone = phone.getText().toString().trim();
            String uHat = hatNum.getText().toString().trim();
            String uCompany = companies.getSelectedItem().toString().trim();

        //creates an object which contains all above strings.
       UserDetails userInfo= new UserDetails(uName,uPhone,uHat,uCompany);
       databaseReference.child(uId).setValue(userInfo);
    }

}
