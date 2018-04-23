package com.iqueues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInProcess extends AppCompatActivity {

    static FirebaseAuth uFirebaseAuth;
    private TextView loginEmail;
    private TextView loginPword;
    private ProgressDialog progressDialog;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_process);

        uFirebaseAuth = uFirebaseAuth.getInstance();

        if(uFirebaseAuth.getCurrentUser() != null) {
            //user is already signin.
            finish();
            startActivity(new Intent(getApplicationContext(),DriverMainScreen.class));
        }
        progressDialog = new ProgressDialog(this);
        loginEmail = (TextView) findViewById(R.id.login_email);
        loginPword = (TextView) findViewById(R.id.login_Pword);

    }


    public void loginUser(View view)
    {
        String email= loginEmail.getText().toString().trim();
        String pWord= loginPword.getText().toString().trim();
        progressDialog.setMessage("loggin in");
        progressDialog.show();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(pWord)) {
            Toast.makeText(SignInProcess.this,"email or password fields are empty",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        uFirebaseAuth.signInWithEmailAndPassword(email,pWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(),DriverMainScreen.class));
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignInProcess.this, "email or password are incorrect,please check your entries.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
//goes to sign-up activity
   public void signUp(View view) {


       Intent intent = new Intent(this,SignUpProcess.class);
       startActivity(intent);
   }

}
