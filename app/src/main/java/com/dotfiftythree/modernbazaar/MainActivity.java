package com.dotfiftythree.modernbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private Button signup,login;
    private EditText email,pass;
    private TextView visitortxt;
    private LinearLayout base;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        base = findViewById(R.id.base);
        email = findViewById(R.id.emailfieldlgin);
        pass = findViewById(R.id.passfieldlgin);
        signup = findViewById(R.id.signupBtn);
        login = findViewById(R.id.loginBtn);
        visitortxt = findViewById(R.id.visitortxt);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(MainActivity.this, Registration.class);
                startActivity(transition);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(pass.getText())) {
                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.fillerror), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.error));
                    snackbar.show();
                } else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Getting you logged in...");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.loginsuccess), Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.success));
                                snackbar.show();
                                progressDialog.cancel();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent transition = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(transition);
                                        finish();
                                    }
                                },300);


                            } else {
                                progressDialog.cancel();
                                Snackbar snackbar = Snackbar.make(base,R.string.invalidcredentials, Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.error));
                                snackbar.show();
                            }
                        }
                    });

                }
            }
        });
        visitortxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(MainActivity.this, Visitor.class);
                startActivity(transition);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser!=null){
            Intent transition = new Intent(MainActivity.this,HomePage.class);
            startActivity(transition);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
