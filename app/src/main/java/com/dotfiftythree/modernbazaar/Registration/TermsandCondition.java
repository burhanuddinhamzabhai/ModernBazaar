package com.dotfiftythree.modernbazaar.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.HomeFragment.HomePage;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TermsandCondition extends AppCompatActivity {
    private Button accept,reject;
    private SharedPreferences userData;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userDB = database.getReference("userDB");
    private HashMap<String, Object> user = new HashMap<>();
    private String userDataKey="";
    private LinearLayout base;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_condition);
        accept = findViewById(R.id.acceptBtn);
        reject = findViewById(R.id.rejectBtn);
        mAuth = FirebaseAuth.getInstance();
        base = findViewById(R.id.base);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(TermsandCondition.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Creating your account...");
                progressDialog.setProgress(0);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(userData.getString(User.getEmail(),null),userData.getString(User.getPassword(),null)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Date currentDate = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                            String dateToStr = format.format(currentDate);
                            user= new HashMap<>();
                            userDataKey=userDB.push().getKey();
                            user.put(User.getEmail(),userData.getString(User.getEmail(),null));
                            user.put(User.getPassword(),userData.getString(User.getPassword(),null));
                            user.put(User.getName(),userData.getString(User.getName(),null));
                            user.put(User.getAge(),userData.getString(User.getAge(),null));
                            user.put(User.getGender(),userData.getString(User.getGender(),null));
                            user.put(User.getStreet(),userData.getString(User.getStreet(),null));
                            user.put(User.getState(),userData.getString(User.getState(),null));
                            user.put(User.getCity(),userData.getString(User.getCity(),null));
                            user.put(User.getCountry(),userData.getString(User.getCountry(),null));
                            user.put(User.getPhone(),userData.getString(User.getPhone(),null));
                            user.put("VERIFICATION",userData.getString(User.getVerified(),null));
                            user.put(User.getTerms(),"Accepted");
                            user.put(User.getUserid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.put("JOINING DATE",dateToStr);
                            userDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.cancel();
                                    Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.accountsuccess),Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(TermsandCondition.this, R.color.success));
                                    snackbar.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent transition = new Intent(TermsandCondition.this, HomePage.class);
                                            startActivity(transition);
                                            finish();
                                        }
                                    },300);



                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.error),Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(TermsandCondition.this, R.color.error));
                                    snackbar.show();
                                }
                            });


                        }else {
                            progressDialog.cancel();
                            Snackbar snackbar = Snackbar.make(base,getResources().getString(R.string.error),Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(TermsandCondition.this, R.color.error));
                            snackbar.show();
                        }
                    }
                });

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(TermsandCondition.this, TandCRejection.class);
                startActivity(transition);
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