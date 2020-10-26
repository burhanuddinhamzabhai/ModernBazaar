package com.dotfiftythree.modernbazaar.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.MainActivity;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.material.snackbar.Snackbar;


public class Registration extends AppCompatActivity {
    private SharedPreferences userData;
    private TextView logintxt;
    private Button continueBtn;
    private EditText email,pass;
    LinearLayout base;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        logintxt = findViewById(R.id.logintxt);
        continueBtn = findViewById(R.id.continueBtn);
        email = findViewById(R.id.emailfieldreg);
        pass = findViewById(R.id.passfieldreg);
        base = findViewById(R.id.base);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        userData =  getSharedPreferences("userData", MODE_PRIVATE);
        if (userData.contains(User.getEmail())){
            email.setText(userData.getString(User.getEmail(),null));
            pass.setText(userData.getString(User.getPassword(),null));

        }

        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor write = userData.edit().clear();
                write.apply();
                Intent transition = new Intent(Registration.this, MainActivity.class);
                startActivity(transition);
                finish();
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(pass.getText()) ){

                Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.fillerror),Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(Registration.this, R.color.error));
                            snackbar.show();

                }else{
                SharedPreferences.Editor write = userData.edit();
                write.putString(User.getEmail(),email.getText().toString());
                write.putString(User.getPassword(),pass.getText().toString());
                write.apply();
                    Intent transition = new Intent(Registration.this, ProfileRegistration.class);
                startActivity(transition);
                finish();
            }
                }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}