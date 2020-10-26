package com.dotfiftythree.modernbazaar.HomeFragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Adapters.BarterProductListAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.BarterProductList;
import com.dotfiftythree.modernbazaar.Constants.Barter;
import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.LoadingImage.GlideApp;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

public class HomeViewInDetail extends AppCompatActivity implements ProductListDialog.ProductListDialogListner {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    DatabaseReference userReference = firebaseDatabase.getReference("userDB");
    public static String productID;
    public static String sellerID, sellerImage;
    DatabaseReference barterCheck = firebaseDatabase.getReference("BarterDB");
    ChildEventListener productDetails, userListener, checksaved, barterCheckListener;
    String userDataKey;
    String phone;
    String mail;
    ImageView productImage, copyProductID, save, copySellerID;
    TextView productName, productDes, productMBSP, periodOfUsage, productCategory, productMrp, productDamageReport,
            productDamageDes, productVerification, productVerificationDes, productShip;
    LinearLayout back, base, homemid, wholehome;
    ProgressDialog progressDialog;
    Boolean phoneCheck = false, mailCheck = false;
    ArrayList<BarterProductList> barterProductLists = new ArrayList<>();
    BarterProductListAdapter barterProductListAdapter;
    private Button makePhone, makeEmail, RequestBarter;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view_in_detail);
        productImage = findViewById(R.id.homedetailproductimage);
        productName = findViewById(R.id.homedetailproductname);
        productDes = findViewById(R.id.homedetailproductdes);
        periodOfUsage = findViewById(R.id.homedetailproductperiod);
        productMBSP = findViewById(R.id.homedetailproductmbsp);
        productCategory = findViewById(R.id.homedetailproductcategory);
        productMrp = findViewById(R.id.homedetailproductmrp);
        productDamageReport = findViewById(R.id.homedetailproductdamagereport);
        productDamageDes = findViewById(R.id.homedetailproductdamagedes);
        productVerification = findViewById(R.id.homedetailproductverification);
        productVerificationDes = findViewById(R.id.homedetailproductverificationdes);
        productShip = findViewById(R.id.homedetailproductshippable);
        back = findViewById(R.id.backtohome);
        copyProductID = findViewById(R.id.copyproductID);
        base = findViewById(R.id.base);
        save = findViewById(R.id.saveproduct);
        copySellerID = findViewById(R.id.copysellerID);
        makePhone = findViewById(R.id.homeViewInDetailcallBtn);
        makeEmail = findViewById(R.id.homeViewInDetailemailBtn);
        RequestBarter = findViewById(R.id.homeViewInDetailReqBarterBtn);
        homemid = findViewById(R.id.homemid);
        wholehome = findViewById(R.id.homewholelayout);
        productID = getIntent().getStringExtra(Product.getProductid());


        copyProductID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ProductID", productID);
                clipboard.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.pidcopy), Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeViewInDetail.this, R.color.blue));
                snackbar.show();
            }
        });
        copySellerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("sellerID", sellerID);
                clipboard.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.sidcopy), Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeViewInDetail.this, R.color.blue));
                snackbar.show();
            }
        });

        barterCheckListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if ((_childValue.get(Barter.getSellerProduct()).toString()).equals(productID)) {
                    if ((_childValue.get(Barter.getBuyer()).toString()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        RequestBarter.setBackground(getDrawable(R.drawable.dark_gray_bg));
                        RequestBarter.setText("Barter Requested");
                        RequestBarter.setEnabled(false);
                        if (snapshot.child(Barter.getBarterResponse()).exists()) {
                            if ((_childValue.get(Barter.getBarterResponse()).toString()).equals("REJECTED")) {
                                RequestBarter.setBackground(getDrawable(R.drawable.primary_btn_bg));
                                RequestBarter.setText("Barter Rejected, Try Again?");
                                RequestBarter.setEnabled(true);
                            }
                        }
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
        barterCheck.addChildEventListener(barterCheckListener);


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
                    GlideApp.with(HomeViewInDetail.this)
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
                        phone = _childValue.get(Product.getPhone()).toString();
                        makePhone.setVisibility(View.VISIBLE);

                    } else {
                        makePhone.setVisibility(View.GONE);
                        homemid.setVisibility(View.GONE);
                    }
                    if (snapshot.child(Product.getEmail()).exists()) {
                        mailCheck = true;
                        mail = _childValue.get(Product.getEmail()).toString();
                        makeEmail.setVisibility(View.VISIBLE);
                    } else {
                        makeEmail.setVisibility(View.GONE);
                        homemid.setVisibility(View.GONE);
                    }
                    if (!snapshot.child(Product.getPhone()).exists() && !snapshot.child(Product.getEmail()).exists()) {
                        wholehome.setVisibility(View.GONE);
                    } else {
                        wholehome.setVisibility(View.VISIBLE);
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

        checksaved = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id;
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(mAuth.getCurrentUser().getUid())) {
                    if (snapshot.child(User.getSavedItems()).exists()) {
                        id = _childValue.get(User.getSavedItems()).toString();
                        Log.i("id", "onChildAdded: " + id);
                        if (id.contains(productID)) {
                            save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                        }
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
        userReference.addChildEventListener(checksaved);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(HomeViewInDetail.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Saving Product...");
                progressDialog.setProgress(0);
                progressDialog.show();
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                userListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String id, newID, oldID;
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        final String _childKey = snapshot.getKey();
                        final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                        if (_childKey.equals(mAuth.getCurrentUser().getUid())) {
                            if (snapshot.child(User.getSavedItems()).exists()) {
                                id = _childValue.get(User.getSavedItems()).toString();
                                Log.i("id", "onChildAdded: " + id);
                                if (id.contains(productID)) {
                                    progressDialog.setTitle("Removing Product...");
                                    newID = id.replace((productID + ", "), "");
                                    map.clear();
                                    map = new HashMap<>();
                                    userDataKey = userReference.push().getKey();
                                    map.put(User.getSavedItems(), newID);
                                    userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.cancel();
                                            save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                                            Snackbar snackbar = Snackbar.make(base, R.string.removefromsave, Snackbar.LENGTH_SHORT);
                                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeViewInDetail.this, R.color.error));
                                            snackbar.show();
                                        }
                                    });
                                } else {
                                    oldID = _childValue.get(User.getSavedItems()).toString();
                                    map = new HashMap<>();
                                    userDataKey = userReference.push().getKey();
                                    map.put(User.getSavedItems(), oldID + productID + ", ");
                                    userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.cancel();
                                            save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                            Snackbar snackbar = Snackbar.make(base, R.string.addtosave, Snackbar.LENGTH_SHORT);
                                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeViewInDetail.this, R.color.blue));
                                            snackbar.show();
                                        }
                                    });
                                }
                            } else {
                                map = new HashMap<>();
                                userDataKey = userReference.push().getKey();
                                map.put(User.getSavedItems(), productID + ", ");
                                userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.cancel();
                                        save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                        Snackbar snackbar = Snackbar.make(base, R.string.addtosave, Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeViewInDetail.this, R.color.blue));
                                        snackbar.show();
                                    }
                                });
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
                userReference.addChildEventListener(userListener);

            }
        });

        makePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(HomeViewInDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(HomeViewInDetail.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                    return;
                }
                startActivity(call);
            }
        });
        makeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send email", "");
                String[] TO = {mail};
//                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "From Modern Bazaar Android App");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeViewInDetail.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        RequestBarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductListDialog productListDialog = new ProductListDialog();
                productListDialog.show(getSupportFragmentManager(), "product selection dialog");
            }
        });
    }
}