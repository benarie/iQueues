package com.iQueues;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyCode extends AppCompatActivity {

    private String PHONE_NUMBER_TAG = "PHONE_NUMBER_TAG";

    private String phoneNumber;
    private String code;
    private String codeSent;
    private String TAG = "TAG";
    private String prefixedNum;
    String phone;
    EditText insertCodeFirstNumEt;
    EditText insertCodeSecondNumEt;
    EditText insertCodeThridNumEt;
    EditText insertCodeForthNumEt;
    EditText insertCodeFifthtNumEt;
    EditText insertCodeSixthNumEt;
    Button verificationCodeBtn;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);

        insertCodeFirstNumEt = findViewById(R.id.num_1);
        insertCodeSecondNumEt = findViewById(R.id.num_2);
        insertCodeThridNumEt = findViewById(R.id.num_3);
        insertCodeForthNumEt = findViewById(R.id.num_4);
        insertCodeFifthtNumEt = findViewById(R.id.num_5);
        insertCodeSixthNumEt = findViewById(R.id.num_6);
        verificationCodeBtn = findViewById(R.id.verification_code_btn);

        code = insertCodeFirstNumEt.getText().toString()
                + insertCodeSecondNumEt.getText().toString()
                + insertCodeThridNumEt.getText().toString()
                + insertCodeForthNumEt.getText().toString()
                + insertCodeFifthtNumEt.getText().toString()
                + insertCodeSixthNumEt.getText().toString();

        sendVerificationCode();

        verificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }

    private void sendVerificationCode() {

        prefixedNum = "+972 ";
        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER_TAG");
        phone = prefixedNum + phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                VerifyCode.this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };


    private void verifySignInCode() {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(VerifyCode.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(VerifyCode.this, DriverMainActivity.class);
                            startActivity(intent);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyCode.this, "incorrect Verification Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}
