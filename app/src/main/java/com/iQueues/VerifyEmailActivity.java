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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The type Verify email activity.
 */
public class VerifyEmailActivity extends AppCompatActivity {


    private String TAG = "TAG";
    /**
     * The Verification email btn.
     */
    Button verificationEmailBtn;

    /**
     * The FirebaseAuth Instance.
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    /**
     * The User.
     */
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_email);

        Toolbar myToolbar = findViewById(R.id.verification_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        verificationEmailBtn = findViewById(R.id.verify_email_btn);
        TextView senAgain = findViewById(R.id.send_again);


        verificationEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waitForVerification();

            }
        });

        String text = "שלח שוב";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                sendVerificationEmail();
            }
        };

        spannableString.setSpan(clickableSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        senAgain.setText(spannableString);
        senAgain.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * wait For Verification after the user confirm the email
     * move the user to the main activity
     */
    private void waitForVerification() {

        user.reload();
        while (user.isEmailVerified()) {

            if (user.isEmailVerified()) {

                verificationEmailBtn.setClickable(true);
                Toast.makeText(VerifyEmailActivity.this, "תהליך ההרשמה בוצע בהצלחה", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(VerifyEmailActivity.this, DriverMainActivity.class);
                startActivity(intent);

                break;
            }
        }
    }


    /**
     * send Verification Email when user d'ont receive the email
     */
    private void sendVerificationEmail() {

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyEmailActivity.this, "אישור נשלח למייל." + user.getEmail(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(VerifyEmailActivity.this, "נכשלה השליחה למייל.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}






