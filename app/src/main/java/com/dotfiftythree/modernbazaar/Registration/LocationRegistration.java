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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.material.snackbar.Snackbar;

public class LocationRegistration extends AppCompatActivity {
    private SharedPreferences userData;
    private EditText street,city,state,country;
    private LinearLayout back,base;
    private Button continueBtn;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        back = findViewById(R.id.backtoabout);
        continueBtn = findViewById(R.id.continueBtnlocation);
        street = findViewById(R.id.streetAddressField);
        city = findViewById(R.id.cityField);
        state = findViewById(R.id.stateField);
        country = findViewById(R.id. countryField);
        base = findViewById(R.id.base);
        userData = getSharedPreferences("userData",MODE_PRIVATE);
        if(userData.contains(User.getStreet())){
            street.setText(userData.getString(User.getStreet(),null));
            city.setText(userData.getString(User.getCity(),null));
            state.setText(userData.getString(User.getState(),null));
            country.setText(userData.getString(User.getCountry(),null));
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(LocationRegistration.this, ProfileRegistration.class);
                startActivity(transition);
                finish();
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(street.getText()) || TextUtils.isEmpty(city.getText()) || TextUtils.isEmpty(state.getText()) || TextUtils.isEmpty(country.getText())) {
                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.fillerror), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(LocationRegistration.this, R.color.error));
                    snackbar.show();
                } else {
                    SharedPreferences.Editor write = userData.edit();
                    write.putString(User.getStreet(), street.getText().toString());
                    write.putString(User.getCity(), city.getText().toString());
                    write.putString(User.getState(), state.getText().toString());
                    write.putString(User.getCountry(), country.getText().toString());
                    write.apply();
                    Intent transition = new Intent(LocationRegistration.this, PhoneNumberConfirmation.class);
                    startActivity(transition);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent transition = new Intent(LocationRegistration.this, ProfileRegistration.class);
        startActivity(transition);
        finish();
    }
}