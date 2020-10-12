package com.dotfiftythree.modernbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerification extends AppCompatActivity {
    private LinearLayout back,base;
    private Button verify;
    private FirebaseAuth mAuth;
    private String mAuthCredentials;
    private EditText code;
    ProgressDialog progressDialog;
    private SharedPreferences userData;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        back = findViewById(R.id.backtophone);
        verify = findViewById(R.id.verifyBtn);
        code = findViewById(R.id.otpfieldlgin);
        base = findViewById(R.id.base);
        mAuth = FirebaseAuth.getInstance();
        userData = getSharedPreferences("userData",MODE_PRIVATE);
        mAuthCredentials = getIntent().getStringExtra("AuthCredentials");
        phoneNumber = getIntent().getStringExtra("Number");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(OTPVerification.this,PhoneNumberConfirmation.class);
                startActivity(transition);
                finish();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(code.getText())){
                    Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.fillerror),Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(OTPVerification.this, R.color.error));
                    snackbar.show();
                }else {
                    progressDialog = new ProgressDialog(OTPVerification.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Verifying OTP");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    String otp = code.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthCredentials, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.cancel();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVerification", "signInWithCredential:success");
                            SharedPreferences.Editor write = userData.edit();
                            write.putString(User.getPhone(),phoneNumber);
                            write.apply();
                            Intent transition = new Intent(OTPVerification.this,TermsandCondition.class);
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
        Intent transition = new Intent(OTPVerification.this, PhoneNumberConfirmation.class);
        startActivity(transition);
        finish();
    }
}
