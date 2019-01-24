package com.iQueues;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String PHONE_NUMBER_TAG = "PHONE_NUMBER_TAG";

    private String phoneNumber;
    private String code;
    private String verificationId;
    private String TAG = "TAG";
    private String prefixedNum;
    private String number;
    TextView senCodeAgain;
    EditText insertCodeFirstNumEt;
    EditText insertCodeSecondNumEt;
    EditText insertCodeThridNumEt;
    EditText insertCodeForthNumEt;
    EditText insertCodeFifthtNumEt;
    EditText insertCodeSixthNumEt;
    Button verificationCodeBtn;
    private String num_1;
    private String num_2;
    private String num_3;
    private String num_4;
    private String num_5;
    private String num_6;
    private FirebaseUser prevUser;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);

        Toolbar myToolbar = findViewById(R.id.verification_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        insertCodeFirstNumEt = findViewById(R.id.num_1);
        insertCodeSecondNumEt = findViewById(R.id.num_2);
        insertCodeThridNumEt = findViewById(R.id.num_3);
        insertCodeForthNumEt = findViewById(R.id.num_4);
        insertCodeFifthtNumEt = findViewById(R.id.num_5);
        insertCodeSixthNumEt = findViewById(R.id.num_6);
        verificationCodeBtn = findViewById(R.id.verification_code_btn);
        senCodeAgain = findViewById(R.id.send_again);

        num_1 = insertCodeFirstNumEt.getText().toString();
        num_2 = insertCodeSecondNumEt.getText().toString();
        num_3 = insertCodeThridNumEt.getText().toString();
        num_4 = insertCodeForthNumEt.getText().toString();
        num_5 = insertCodeFifthtNumEt.getText().toString();
        num_6 = insertCodeSixthNumEt.getText().toString();

        code = num_1 + num_2 + num_3 + num_4 + num_5 + num_6;

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER_TAG");

        sendVerificationCode(phoneNumber);

        verificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num_1.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num_2.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num_3.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num_4.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num_5.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num_6.isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "שדה הסיסמה ריק", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(code);

            }
        });

        String text = "שלח שוב";

        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                sendVerificationCode(phoneNumber);
            }
        };

        spannableString.setSpan(clickableSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        senCodeAgain.setText(spannableString);
        senCodeAgain.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void sendVerificationCode(String phoneNumber) {

        prefixedNum = "+972";
        number = prefixedNum + phoneNumber;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VerifyPhoneActivity.this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };


    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithAuthCredential(credential);
    }

    private void signInWithAuthCredential(PhoneAuthCredential credential) {

        prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(VerifyPhoneActivity.this, "ההרשמה התבצעה בהצלחה.", Toast.LENGTH_SHORT).show();

                            prevUser = task.getResult().getUser();

                            Intent intent = new Intent(VerifyPhoneActivity.this, DriverMainActivity.class);
                            startActivity(intent);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneActivity.this, "קוד הSMS לא הוזן בהצלחה", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isComplete()) {
//
//                            prevUser = task.getResult().getUser();
//                            // Merge prevUser and currentUser accounts and data
//
//
//                            Intent intent = new Intent(VerifyPhoneActivity.this, DriverMainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                Toast.makeText(VerifyPhoneActivity.this, "incorrect Verification Code", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
}




