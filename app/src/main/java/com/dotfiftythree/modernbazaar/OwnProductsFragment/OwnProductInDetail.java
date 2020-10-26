package com.dotfiftythree.modernbazaar.OwnProductsFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dotfiftythree.modernbazaar.Adapters.BarterProductListAdapter;
import com.dotfiftythree.modernbazaar.Adapters.OwnProductsAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.BarterProductList;
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

import java.util.ArrayList;
import java.util.HashMap;

public class OwnProductInDetail extends AppCompatActivity {

    static String productID;
    static String sellerID, sellerImage;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    DatabaseReference userReference = firebaseDatabase.getReference("userDB");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    DatabaseReference barterCheck = firebaseDatabase.getReference("BarterDB");
    ChildEventListener productDetails, userListener, checksaved, barterCheckListener;
    String userDataKey;
    String phone;
    String mail, mbsp;
    ImageView productImage, copyProductID, save, copySellerID;
    TextView productName, productDes, productMBSP, periodOfUsage, productCategory, productMrp, productDamageReport,
            productDamageDes, productVerification, productVerificationDes, productShip, productContact;
    LinearLayout back, base, homemid, wholehome;
    ProgressDialog progressDialog;
    Boolean phoneCheck = false, mailCheck = false;
    ArrayList<BarterProductList> barterProductLists = new ArrayList<>();
    BarterProductListAdapter barterProductListAdapter;
    OwnProductsAdapter ownProductsAdapter;
    private Button barterthis, editBtn, deleteBtn;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_product_in_detail);
        productImage = findViewById(R.id.ownpdetailproductimage);
        productName = findViewById(R.id.ownpdetailproductname);
        productDes = findViewById(R.id.ownpdetailproductdes);
        periodOfUsage = findViewById(R.id.ownpdetailproductperiod);
        productMBSP = findViewById(R.id.ownpdetailproductmbsp);
        productCategory = findViewById(R.id.ownpdetailproductcategory);
        productMrp = findViewById(R.id.ownpdetailproductmrp);
        productDamageReport = findViewById(R.id.ownpdetailproductdamagereport);
        productDamageDes = findViewById(R.id.ownpdetailproductdamagedes);
        productVerification = findViewById(R.id.ownpdetailproductverification);
        productVerificationDes = findViewById(R.id.ownpdetailproductverificationdes);
        productShip = findViewById(R.id.ownpdetailproductshippable);
        back = findViewById(R.id.backfromproductdetail);
        base = findViewById(R.id.base);
        barterthis = findViewById(R.id.ownProductbarterthis);
        editBtn = findViewById(R.id.ownpdetailedtbtn);
        productContact = findViewById(R.id.ownpdetailproductcontact);

        productID = getIntent().getStringExtra(Product.getProductid());

        productDetails = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String image;
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if ((_childValue.get(Product.getProductid()).toString()).equals(productID)) {
                    image = _childValue.get(Product.getImage()).toString();
                    sellerImage = image;
                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(sellerImage);
                    GlideApp.with(OwnProductInDetail.this)
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
                    productShip.setText(getString(R.string.ship) + " : " + _childValue.get(Product.getShip()).toString());
                    sellerID = _childValue.get(Product.getUserid()).toString();
                    if (snapshot.child(Product.getPhone()).exists()) {
                        phoneCheck = true;
                        productContact.setText(getString(R.string.productcontactshared) + " : " + getString(R.string.number));


                    }
                    if (snapshot.child(Product.getEmail()).exists()) {
                        mailCheck = true;
                        productContact.setText(getString(R.string.productcontactshared) + " : " + getString(R.string.email));

                    }
                    if (!snapshot.child(Product.getPhone()).exists() && !snapshot.child(Product.getEmail()).exists()) {
                        productContact.setVisibility(View.GONE);
                    } else {
                        productContact.setVisibility(View.VISIBLE);
                        productContact.setText(getString(R.string.productcontactshared) + " : " + getString(R.string.number) + " & " + getString(R.string.email));
                    }
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
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(OwnProductInDetail.this, EditOwnProduct.class);
                transition.putExtra(Product.getProductid(), productID);
                startActivity(transition);
                finish();
            }
        });

        barterthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(OwnProductInDetail.this, ProductBasedBarter.class);
                transition.putExtra(Product.getProductid(), productID);
                startActivity(transition);
                finish();
            }
        });

    }
}