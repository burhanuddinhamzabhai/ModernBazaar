package com.dotfiftythree.modernbazaar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FileAComplain extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference complainDB = firebaseDatabase.getReference("ComplainDB");
    HashMap<String, Object> map = new HashMap<>();
    String complainString;
    ProgressDialog progressDialog;
    private EditText userID, sellerID, productID, barterID, complainDes;
    private Button regComplain;
    private LinearLayout base, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_a_complain);
        userID = findViewById(R.id.complainUserID);
        sellerID = findViewById(R.id.complainSellerID);
        productID = findViewById(R.id.complainProductID);
        barterID = findViewById(R.id.complainBarterID);
        complainDes = findViewById(R.id.complainDescription);
        regComplain = findViewById(R.id.regComplainBtn);
        base = findViewById(R.id.base);
        back = findViewById(R.id.backfromcomplain);


        userID.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());

        regComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(sellerID.getText()) && !TextUtils.isEmpty(complainDes.getText())) {
                    progressDialog = new ProgressDialog(FileAComplain.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Registering your complain...");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String dateToStr = simpleDateFormat.format(date);
                    map = new HashMap<>();
                    complainString = complainDB.push().getKey();
                    map.put(Complain.getUserid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put(Complain.getSellerid(), sellerID.getText().toString());
                    if (!TextUtils.isEmpty(productID.getText().toString())) {
                        map.put(Complain.getProductid(), productID.getText().toString());
                    }
                    if (!TextUtils.isEmpty(barterID.getText())) {
                        map.put(Complain.getBarterid(), barterID.getText().toString());
                    }
                    map.put(Complain.getComplaindes(), complainDes.getText().toString());
                    complainDB.child(complainString).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            map.clear();
                            progressDialog.cancel();
                            Snackbar snackbar = Snackbar.make(base, R.string.registerdsuccess, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(FileAComplain.this, R.color.success));
                            snackbar.show();
                        }
                    });

                } else {
                    Snackbar snackbar = Snackbar.make(base, R.string.fillallfield, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(FileAComplain.this, R.color.error));
                    snackbar.show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}