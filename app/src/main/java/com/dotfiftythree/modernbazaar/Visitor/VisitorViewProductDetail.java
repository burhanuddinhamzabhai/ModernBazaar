package com.dotfiftythree.modernbazaar.Visitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.LoadingImage.GlideApp;
import com.dotfiftythree.modernbazaar.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class VisitorViewProductDetail extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productDetails;
    String productID;
    ImageView productImage;
    TextView productName, productDes, productMBSP, periodOfUsage, productCategory, productMrp, productDamageReport,
            productDamageDes, productVerification, productVerificationDes, productShip;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_view_product_detail);
        productImage = findViewById(R.id.visitordetailproductimage);
        productName = findViewById(R.id.visitordetailproductname);
        productDes = findViewById(R.id.visitordetailproductdes);
        periodOfUsage = findViewById(R.id.visitordetailproductperiod);
        productMBSP = findViewById(R.id.visitordetailproductmbsp);
        productCategory = findViewById(R.id.visitordetailproductcategory);
        productMrp = findViewById(R.id.visitordetailproductmrp);
        productDamageReport = findViewById(R.id.visitordetailproductdamagereport);
        productDamageDes = findViewById(R.id.visitordetailproductdamagedes);
        productVerification = findViewById(R.id.visitordetailproductverification);
        productVerificationDes = findViewById(R.id.visitordetailproductverificationdes);
        productShip = findViewById(R.id.visitordetailproductshippable);
        back = findViewById(R.id.backtovisitor);

        productID = getIntent().getStringExtra(Product.getProductid());

        productDetails = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if ((_childValue.get(Product.getProductid()).toString()).equals(productID)) {
                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(_childValue.get(Product.getImage()).toString());
                    GlideApp.with(VisitorViewProductDetail.this)
                            .load(ref)
                            .into(productImage);
                    productName.setText(_childValue.get(Product.getName()).toString());
                    if (_childValue.get(Product.getPurchaseYear()).equals("")) {
                        periodOfUsage.setText(_childValue.get(Product.getPurchaseMonth()).toString() + " Months");
                    } else if (_childValue.get(Product.getPurchaseMonth()).equals("")) {
                        periodOfUsage.setText(_childValue.get(Product.getPurchaseYear()).toString() + " Years");
                    } else {
                        periodOfUsage.setText(_childValue.get(Product.getPurchaseYear()).toString() + " Years " + _childValue.get(Product.getPurchaseMonth()).toString() + " Months");
                    }
                    productMBSP.setText(_childValue.get(Product.getMbsp()).toString());
                    productDes.setText(_childValue.get(Product.getDescription()).toString());
                    productCategory.setText(getString(R.string.productcat) + " : " + _childValue.get(Product.getCategory()).toString());
                    productMrp.setText(getString(R.string.mrp) + " : " + _childValue.get(Product.getMrp()).toString());
                    productDamageReport.setText(getString(R.string.isdamage) + " : " + _childValue.get(Product.getDamaged()).toString());
                    if (_childKey.contains(Product.getDamageReport())) {
                        productDamageDes.setText(getString(R.string.damagereport) + " : " + _childValue.get(Product.getDamageReport()).toString());
                    } else {
                        productDamageDes.setVisibility(View.GONE);
                    }
                    productVerification.setText(getString(R.string.verification) + " : " + _childValue.get(Product.getVerification()).toString());
                    if (_childKey.contains(Product.getVerificationReport())) {
                        productVerificationDes.setText(getString(R.string.verificationreport) + " : " + _childValue.get(Product.getVerificationReport()).toString());
                    } else {
                        productVerificationDes.setVisibility(View.GONE);
                    }
                    productShip.setText(getString(R.string.ship) + " : " + _childValue.get(Product.getShip()).toString());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(productDetails);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(VisitorViewProductDetail.this, Visitor.class);
                startActivity(transition);
                finish();
            }
        });
    }
}