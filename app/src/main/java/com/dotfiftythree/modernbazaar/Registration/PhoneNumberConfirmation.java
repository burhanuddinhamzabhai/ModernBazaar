package com.dotfiftythree.modernbazaar.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberConfirmation extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView countryCode,phoneNumber;
    private ProgressDialog progressDialog;
    private LinearLayout back,base;
    private Button generateOTP;
    String completeNumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_confirmation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        back = findViewById(R.id.backtolocation);
        base = findViewById(R.id.base);
        generateOTP = findViewById(R.id.generateBtn);
        countryCode = findViewById(R.id.countryCodeField);
        phoneNumber = findViewById(R.id.phoneNumberField);
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(PhoneNumberConfirmation.this, LocationRegistration.class);
                startActivity(transition);
                finish();
            }
        });
        generateOTP.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(countryCode.getText()) || TextUtils.isEmpty(phoneNumber.getText())) {
                    Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.fillerror),Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(PhoneNumberConfirmation.this, R.color.error));
                    snackbar.show();
                } else {
                    String code = countryCode.getText().toString();
                    String number = phoneNumber.getText().toString();
                    completeNumber = "+" + code + number;
                    progressDialog = new ProgressDialog(PhoneNumberConfirmation.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Generating OTP");
                    progressDialog.setProgress(0);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            completeNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneNumberConfirmation.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks


                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
                progressDialog.cancel();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i("code error", "onVerificationFailed: "+e);
                progressDialog.cancel();
            }

            @Override
            public void onCodeSent(@NonNull final String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.cancel();
                                        Intent transition = new Intent(PhoneNumberConfirmation.this, OTPVerification.class);
                                        transition.putExtra("AuthCredentials",verificationId);
                                        transition.putExtra("Number",completeNumber);
                                        startActivity(transition);
                                        finish();
                                    }
                                },10000);


            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneNumberConfirmation.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.cancel();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVerification", "signInWithCredential:success");
                            Intent transition = new Intent(PhoneNumberConfirmation.this, TermsandCondition.class);
                            startActivity(transition);
                            finish();

                        } else {
                            progressDialog.cancel();
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVerification", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent transition = new Intent(PhoneNumberConfirmation.this, ProfileRegistration.class);
        startActivity(transition);
        finish();
    }
}