package com.dotfiftythree.modernbazaar.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.material.snackbar.Snackbar;

public class ProfileRegistration extends AppCompatActivity {
    private SharedPreferences userData;
    private LinearLayout back;
    private Button continueBtn;
    private RadioGroup gender;
    private RadioButton male,female,other;
    User user;
    private EditText name, age;
    private String userGender="";
    LinearLayout base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        back = findViewById(R.id.back);
        continueBtn = findViewById(R.id.continueBtnabout);
        name = findViewById(R.id.namefieldreg);
        age = findViewById(R.id.agefieldreg);
        gender = findViewById(R.id.gendergroup);
        male = findViewById(R.id.genderMale);
        female = findViewById(R.id.genderFemale);
        other = findViewById(R.id.genderother);
        base = findViewById(R.id.base);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        if (userData.contains(User.getName())) {
            name.setText(userData.getString(User.getName(), null));
            age.setText(userData.getString(User.getAge(), null));
            if(userData.contains(User.getGender())) {
                if (userData.getString(User.getGender(), null).equals("male")) {
                    userGender="male";
                    male.setChecked(true);
                } else if (userData.getString(User.getGender(), null).equals("female")) {
                    userGender="female";
                    female.setChecked(true);
                } else if (userData.getString(User.getGender(), null).equals("other")) {
                    userGender="other";
                    other.setChecked(true);
                }
            }
        }
        //checking gender selected by user
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.genderMale:
                        userGender = "male";
                        break;
                    case R.id.genderFemale:
                        userGender = "female";
                        break;

                    case R.id.genderother:
                        userGender = "other";
                        break;

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(ProfileRegistration.this, Registration.class);
                startActivity(transition);
                finish();
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(age.getText()) || TextUtils.isEmpty(userGender)) {
                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.fillerror), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(ProfileRegistration.this, R.color.error));
                    snackbar.show();
                } else {
                    SharedPreferences.Editor write = userData.edit();
                    write.putString(User.getName(), name.getText().toString());
                    write.putString(User.getAge(), age.getText().toString());
                    write.putString(User.getGender(), userGender);
                    write.apply();
                    Intent transition = new Intent(ProfileRegistration.this, LocationRegistration.class);
                    startActivity(transition);
                    finish();
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent transition = new Intent(ProfileRegistration.this, Registration.class);
        startActivity(transition);
        finish();
    }
}