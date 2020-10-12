package com.dotfiftythree.modernbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TandCRejection extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button back, exit;
    private SharedPreferences userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tand_c_rejection);
        back = findViewById(R.id.backtotc);
        exit = findViewById(R.id.exitbtn);
        mAuth = FirebaseAuth.getInstance();
        userData = getSharedPreferences("userData",MODE_PRIVATE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(TandCRejection.this,TermsandCondition.class);
                startActivity(transition);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences.Editor write = userData.edit().clear();
                write.apply();
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
        finish();
    }
}