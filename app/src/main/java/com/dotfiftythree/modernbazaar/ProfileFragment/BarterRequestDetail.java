package com.dotfiftythree.modernbazaar.ProfileFragment;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BarterRequestDetail extends AppCompatActivity {
    static String productID, barterID;
    static String sellerID, sellerImage;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    DatabaseReference userReference = firebaseDatabase.getReference("userDB");
    DatabaseReference barter = firebaseDatabase.getReference("BarterDB");
    ChildEventListener productDetails, userListener, checksaved, barterListener;
    String userDataKey;
    String phone;
    String mail;
    ImageView productImage, copyProductID, save, copySellerID;
    TextView productName, productDes, productMBSP, periodOfUsage, productCategory, productMrp, productDamageReport,
            productDamageDes, productVerification, productVerificationDes, productShip;
    LinearLayout back, base, homemid, wholehome, barterdetail1mid;
    ProgressDialog progressDialog;
    Boolean phoneCheck = false, mailCheck = false;
    ArrayList<BarterProductList> barterProductLists = new ArrayList<>();
    BarterProductListAdapter barterProductListAdapter;
    private Button makePhone, makeEmail, acceptBarter, rejectBarter;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_request_detail);
        productImage = findViewById(R.id.barterdetailproductimage);
        productName = findViewById(R.id.barterdetailproductname);
        productDes = findViewById(R.id.barterdetailproductdes);
        periodOfUsage = findViewById(R.id.barterdetailproductperiod);
        productMBSP = findViewById(R.id.barterdetailproductmbsp);
        productCategory = findViewById(R.id.barterdetailproductcategory);
        productMrp = findViewById(R.id.barterdetailproductmrp);
        productDamageReport = findViewById(R.id.barterdetailproductdamagereport);
        productDamageDes = findViewById(R.id.barterdetailproductdamagedes);
        productVerification = findViewById(R.id.barterdetailproductverification);
        productVerificationDes = findViewById(R.id.barterdetailproductverificationdes);
        productShip = findViewById(R.id.barterdetailproductshippable);
        back = findViewById(R.id.backfrombarterdetail);
        copyProductID = findViewById(R.id.barterdetailcopyproductID);
        base = findViewById(R.id.base);
        save = findViewById(R.id.saveproductbarterdetail);
        copySellerID = findViewById(R.id.barterdetailcopysellerID);
        makePhone = findViewById(R.id.barterdetailcallBtn);
        makeEmail = findViewById(R.id.barterdetailemailBtn);
        acceptBarter = findViewById(R.id.barterdetailacceptbarter);
        rejectBarter = findViewById(R.id.barterdetailrejbarter);
        homemid = findViewById(R.id.barterdetailmid);
        wholehome = findViewById(R.id.barterdetailwholelayout);
        barterdetail1mid = findViewById(R.id.barterdetail1mid);
        productID = getIntent().getStringExtra(Product.getProductid());
        barterID = getIntent().getStringExtra(Barter.getBarterID());
        Log.i("barter", barterID);

        copyProductID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ProductID", productID);
                clipboard.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.pidcopy), Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.blue));
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
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.blue));
                snackbar.show();
            }
        });


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
                    GlideApp.with(BarterRequestDetail.this)
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

        barterListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(barterID)) {
                    if (snapshot.child(Barter.getBuyerMobile()).exists()) {
                        phoneCheck = true;
                        phone = _childValue.get(Barter.getBuyerMobile()).toString();
                        makePhone.setVisibility(View.VISIBLE);

                    } else {
                        makePhone.setVisibility(View.GONE);
                        homemid.setVisibility(View.GONE);
                    }
                    if (snapshot.child(Barter.getBuyerMail()).exists()) {
                        mailCheck = true;
                        mail = _childValue.get(Barter.getBuyerMail()).toString();
                        makeEmail.setVisibility(View.VISIBLE);
                    } else {
                        makeEmail.setVisibility(View.GONE);
                        homemid.setVisibility(View.GONE);
                    }
                    if (!(snapshot.child(Barter.getBuyerMobile()).exists()) && !(snapshot.child(Barter.getBuyerMail()).exists())) {
                        wholehome.setVisibility(View.GONE);
                    } else {
                        wholehome.setVisibility(View.VISIBLE);
                    }
                    if (snapshot.child(Barter.getBarterResponse()).exists()) {
                        if ((_childValue.get(Barter.getBarterResponse()).toString()).equals("ACCEPTED")) {
                            rejectBarter.setVisibility(View.GONE);
                            acceptBarter.setText(R.string.barteraccepted);
                            acceptBarter.setEnabled(false);
                            barterdetail1mid.setVisibility(View.GONE);
                        } else if ((_childValue.get(Barter.getBarterResponse()).toString()).equals("REJECTED")) {
                            acceptBarter.setVisibility(View.GONE);
                            rejectBarter.setText(R.string.barterrejected);
                            rejectBarter.setEnabled(false);
                            barterdetail1mid.setVisibility(View.GONE);
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
        barter.addChildEventListener(barterListener);

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
                    if (snapshot.child("SAVED ITEMS").exists()) {
                        id = _childValue.get("SAVED ITEMS").toString();
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
                progressDialog = new ProgressDialog(BarterRequestDetail.this);
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
                            if (snapshot.child("SAVED ITEMS").exists()) {
                                id = _childValue.get("SAVED ITEMS").toString();
                                Log.i("id", "onChildAdded: " + id);
                                if (id.contains(productID)) {
                                    progressDialog.setTitle("Removing Product...");
                                    newID = id.replace((productID + ", "), "");
                                    map.clear();
                                    map = new HashMap<>();
                                    userDataKey = userReference.push().getKey();
                                    map.put("SAVED ITEMS", newID);
                                    userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.cancel();
                                            save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                                            Snackbar snackbar = Snackbar.make(base, R.string.removefromsave, Snackbar.LENGTH_SHORT);
                                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.error));
                                            snackbar.show();
                                        }
                                    });
                                } else {
                                    oldID = _childValue.get("SAVED ITEMS").toString();
                                    map = new HashMap<>();
                                    userDataKey = userReference.push().getKey();
                                    map.put("SAVED ITEMS", oldID + productID + ", ");
                                    userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.cancel();
                                            save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                            Snackbar snackbar = Snackbar.make(base, R.string.addtosave, Snackbar.LENGTH_SHORT);
                                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.blue));
                                            snackbar.show();
                                        }
                                    });
                                }
                            } else {
                                map = new HashMap<>();
                                userDataKey = userReference.push().getKey();
                                map.put("SAVED ITEMS", productID + ", ");
                                userReference.child(_childKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.cancel();
                                        save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                        Snackbar snackbar = Snackbar.make(base, R.string.addtosave, Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.blue));
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
                if (ActivityCompat.checkSelfPermission(BarterRequestDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(BarterRequestDetail.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

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
                    Toast.makeText(BarterRequestDetail.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        acceptBarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = simpleDateFormat.format(date);

                map = new HashMap<>();
                map.put(Barter.getBarterResponse(), "ACCEPTED");
                map.put(Barter.getResponseDate(), dateToStr);
                barter.child(barterID).updateChildren(map);
                map.clear();
                map = new HashMap<>();
                map.put(Barter.getResponseDate(), dateToStr);
                map.put(Product.getBarter(), "DONE");
                databaseReference.child(productID).updateChildren(map);
                map.clear();
                rejectBarter.setVisibility(View.GONE);
                acceptBarter.setText(R.string.barteraccepted);
                acceptBarter.setEnabled(false);
                barterdetail1mid.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(base, R.string.barteraccepted, Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.success));
                snackbar.show();
            }
        });
        rejectBarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = simpleDateFormat.format(date);

                map = new HashMap<>();
                map.put(Barter.getBarterResponse(), "REJECTED");
                map.put(Barter.getResponseDate(), dateToStr);
                barter.child(barterID).updateChildren(map);
                map.clear();
                acceptBarter.setVisibility(View.GONE);
                rejectBarter.setText(R.string.barterrejected);
                rejectBarter.setEnabled(false);
                barterdetail1mid.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(base, R.string.barterrejected, Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(BarterRequestDetail.this, R.color.error));
                snackbar.show();
            }
        });
    }
}