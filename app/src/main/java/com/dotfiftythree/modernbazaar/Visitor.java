package com.dotfiftythree.modernbazaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Visitor extends AppCompatActivity {
    private TextView mbspinfo,signup;
    private LinearLayout mbspdes,base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        mbspinfo = findViewById(R.id.visitormbspinfo);
        mbspdes = findViewById(R.id.visitormbspdetail);
        signup = findViewById(R.id.visitorsignup);
        base = findViewById(R.id.base);
        mbspinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbspdes.getVisibility()==View.GONE){
                    mbspdes.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.mbspclickagain),Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(Visitor.this, R.color.blue));
                    snackbar.show();
                }else
                {
                    mbspdes.setVisibility(View.GONE);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(Visitor.this,Registration.class);
                startActivity(transition);
                finish();
            }
        });
    }
}