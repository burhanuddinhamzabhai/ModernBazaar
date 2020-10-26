package com.dotfiftythree.modernbazaar.OwnProductsFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.Constants.User;
import com.dotfiftythree.modernbazaar.LoadingImage.GlideApp;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditOwnProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int GALLERY_REQUEST_CODE = 123;
    String[] categorylist = {"Laptop", "Computer", "Printer", "Scanner", "Mobile", "Tablet", "Camera",
            "Tv", "Fridge", "Wall AC", "Tower AC", "Cooler", "Oven", "Furniture"};
    String[] verificationlist = {"Request", "Unverified"};
    String productImageUri, productcategory = "Laptop", productverification = "Request", productmbspvalue, productID, fetchedImage;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference product = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productListener, userListener;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LinearLayout back, selectImage, damagereportlin, sharelin, base;
    private ImageView productImage;
    private RadioGroup damage, shareContactGrp, shipping;
    private RadioButton damageYes, damageNo, shareYes, shareNo, shipYes, shipNo;
    private ScrollView productDetails;
    private Button calculatembsp, save, cancel;
    private CheckBox shareemail, sharephone;
    private Spinner categoryspin, verificationspin;
    private EditText productName, productDes, productMrp, purchaseYear, purchaseMonth, damageReport, productmbsp;
    private Uri imagedata;
    private ProgressDialog progressDialog;
    private DatabaseReference userDB = firebaseDatabase.getReference("userDB");
    private HashMap<String, Object> map = new HashMap<>();
    private String productString = "", userName, userPhone;
    private boolean damagebool, shipbool, sharebool, emailbool, phonebool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_own_product);
        back = findViewById(R.id.backfromownproductedit);
        selectImage = findViewById(R.id.ownproducteditchangeimage);
        productImage = findViewById(R.id.ownproductedituploadproductimage);
        productName = findViewById(R.id.ownproducteditproductnameupload);
        productDes = findViewById(R.id.ownproducteditproductdesupload);
        purchaseYear = findViewById(R.id.ownproducteditproductbuyyearupload);
        purchaseMonth = findViewById(R.id.ownproducteditproductbuymonthupload);
        productMrp = findViewById(R.id.ownproducteditproductmrpupload);
        damageReport = findViewById(R.id.ownproducteditdamagedesupload);
        productmbsp = findViewById(R.id.ownproducteditproductmbspupload);
        damage = findViewById(R.id.ownproducteditdamagegroupupload);
        damageYes = findViewById(R.id.ownproducteditdamageyesupload);
        damageNo = findViewById(R.id.ownproducteditdamagenoupload);
        damagereportlin = findViewById(R.id.damagereportlin);
        sharelin = findViewById(R.id.ownproducteditsharecontactuploadlin);
        shareContactGrp = findViewById(R.id.ownproducteditsharegroupupload);
        shareYes = findViewById(R.id.ownproducteditshareyesupload);
        shareNo = findViewById(R.id.ownproducteditsharenoupload);
        shipping = findViewById(R.id.ownproducteditshipgroupupload);
        shipYes = findViewById(R.id.ownproducteditshipyesupload);
        shipNo = findViewById(R.id.ownproducteditshipnoupload);
        base = findViewById(R.id.base);
        shareemail = findViewById(R.id.ownproducteditshareemailupload);
        sharephone = findViewById(R.id.ownproducteditsharephoneupload);
        calculatembsp = findViewById(R.id.ownproducteditcalculatembspupload);
        categoryspin = findViewById(R.id.ownproducteditcatergorylistupload);
        verificationspin = findViewById(R.id.ownproducteditverificationlistupload);
        save = findViewById(R.id.ownproducteditsaveproductbtn);
        cancel = findViewById(R.id.ownproducteditcancelproductuploadbtn);


        productID = getIntent().getStringExtra(Product.getProductid());

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner categoryspin = findViewById(R.id.ownproducteditcatergorylistupload);
        categoryspin.setOnItemSelectedListener(EditOwnProduct.this);
        Spinner verificationspin = findViewById(R.id.ownproducteditverificationlistupload);
        verificationspin.setOnItemSelectedListener(EditOwnProduct.this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a1 = new ArrayAdapter(EditOwnProduct.this, android.R.layout.simple_spinner_item, categorylist);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        categoryspin.setAdapter(a1);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a2 = new ArrayAdapter(EditOwnProduct.this, android.R.layout.simple_spinner_item, verificationlist);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        verificationspin.setAdapter(a2);


        //fetching current product details
        this.productListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(productID)) {
                    fetchedImage = _childValue.get(Product.getImage()).toString();
                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fetchedImage);
                    GlideApp.with(EditOwnProduct.this)
                            .load(ref)
                            .into(productImage);
                    productName.setText(_childValue.get(Product.getName()).toString());
                    productDes.setText(_childValue.get(Product.getDescription()).toString());
                    productMrp.setText(_childValue.get(Product.getMrp()).toString());
                    purchaseYear.setText(_childValue.get(Product.getPurchaseYear()).toString());
                    purchaseMonth.setText(_childValue.get(Product.getPurchaseMonth()).toString());
                    if ((_childValue.get(Product.getDamaged()).toString()).equals("true")) {
                        damageYes.setChecked(true);
                        damageNo.setChecked(false);
                        damagereportlin.setVisibility(View.VISIBLE);
                        damageReport.setText(_childValue.get(Product.getDamageReport()).toString());
                    } else {
                        damageYes.setChecked(false);
                        damageNo.setChecked(true);
                        damagereportlin.setVisibility(View.GONE);
                    }
                    productmbsp.setText(_childValue.get(Product.getMbsp()).toString());
                    if ((_childValue.get(Product.getShip()).toString()).equals("true")) {
                        shipYes.setChecked(true);
                        shipNo.setChecked(false);
                    } else {
                        shipYes.setChecked(false);
                        shipNo.setChecked(true);
                    }
                    if ((_childValue.get(Product.getShareContact()).toString()).equals("true")) {
                        shareYes.setChecked(true);
                        shareNo.setChecked(false);
                        sharelin.setVisibility(View.VISIBLE);
                        if (snapshot.child(Product.getPhone()).exists()) {
                            sharephone.setChecked(true);
                        }
                        if (snapshot.child(Product.getEmail()).exists()) {
                            shareemail.setChecked(true);
                        }
                    } else {
                        shareYes.setChecked(false);
                        shareNo.setChecked(true);
                        sharelin.setVisibility(View.GONE);

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
        product.addChildEventListener(productListener);


//fetching current user data
        userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String Name = "", Phone = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                if (_childKey.equals(firebaseAuth.getCurrentUser().getUid())) {
                    Name = _childValue.get(User.getName()).toString();
                    Phone = _childValue.get(User.getPhone()).toString();
                    Log.i("name", "onChildAdded: " + userName);
                    Log.i("num", "onChildAdded: " + userPhone);
                    userName = Name;
                    userPhone = Phone;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userDB.addChildEventListener(userListener);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(EditOwnProduct.this, OwnProductInDetail.class);
                transition.putExtra(Product.getProductid(), productID);
                startActivity(transition);
                finish();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImage = new Intent();
                pickImage.setType("image/*");
                pickImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(pickImage, "Pick Product Image"), GALLERY_REQUEST_CODE);

            }
        });
        //checking damage selected by user
        damage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ownproducteditdamageyesupload:
                        damagereportlin.setVisibility(View.VISIBLE);
                        damagebool = true;
                        break;
                    case R.id.ownproducteditdamagenoupload:
                        damagereportlin.setVisibility(View.GONE);
                        damagebool = false;
                        break;

                }
            }
        });
        //checking contact sharing selected by user
        shareContactGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ownproducteditshareyesupload:
                        sharebool = true;
                        sharelin.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.swipeup), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.blue));
                        snackbar.show();
                        break;
                    case R.id.ownproducteditsharenoupload:
                        sharebool = false;
                        sharelin.setVisibility(View.GONE);
                        shareemail.setChecked(false);
                        sharephone.setChecked(false);
                        break;

                }
            }
        });
        //checking shipping selected by user
        shipping.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ownproducteditshipyesupload:
                        shipbool = true;
                        break;
                    case R.id.ownproducteditshipnoupload:
                        shipbool = false;
                        break;

                }
            }
        });
        calculatembsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbspCalculate();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProduct();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(EditOwnProduct.this, OwnProductInDetail.class);
                transition.putExtra(Product.getProductid(), productID);
                startActivity(transition);
                finish();
            }
        });

    }


    private void mbspCalculate() {
        if (!TextUtils.isEmpty(productMrp.getText()) && (!TextUtils.isEmpty(purchaseYear.getText()) || !TextUtils.isEmpty(purchaseMonth.getText())) && (damageYes.isChecked() || damageNo.isChecked())) {
            if (TextUtils.isEmpty(purchaseYear.getText()) && ((Integer.parseInt(purchaseMonth.getText().toString())) <= 5)) {
                if (damageYes.isChecked()) {
                    int minus = Integer.parseInt(purchaseMonth.getText().toString());
//                    Log.i("m1", "mbspCalculate: " + minus);
                    for (int i = 0; i < (productMrp.length() / 2); i++) {
                        minus = minus * 10;
//                        Log.i("m2", "mbspCalculate: " + minus);
                    }
//                    Log.i("m2", "mbspCalculate: " + minus);
                    productmbspvalue = String.valueOf((((Integer.parseInt(productMrp.getText().toString())) * 70) / 100) - minus);
                    productmbsp.setText(productmbspvalue + " Rs");
                } else if (damageNo.isChecked()) {
                    int minus = Integer.parseInt(purchaseMonth.getText().toString());
//                    Log.i("m1", "mbspCalculate: " + minus);
                    for (int i = 0; i < (productMrp.length() / 2); i++) {
                        minus = minus * 10;
                    }
//                    Log.i("m2", "mbspCalculate: " + minus);
                    productmbspvalue = String.valueOf((Integer.parseInt(productMrp.getText().toString())) - minus);
                    productmbsp.setText(productmbspvalue + " Rs");
                }
            } else if (damageYes.isChecked() && !TextUtils.isEmpty(purchaseYear.getText()) && TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf(((((((Integer.parseInt(productMrp.getText().toString())) * 50) / 100) / (Integer.parseInt((purchaseYear.getText().toString())) * 12)) * 10) * 50) / 100);
                productmbsp.setText(productmbspvalue + " Rs");
            } else if (damageYes.isChecked() && TextUtils.isEmpty(purchaseYear.getText()) && !TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf(((((((Integer.parseInt(productMrp.getText().toString())) * 50) / 100) / (Integer.parseInt(purchaseMonth.getText().toString()))) * 10) * 50) / 100);
                productmbsp.setText(productmbspvalue + " Rs");
            } else if (damageYes.isChecked() && !TextUtils.isEmpty(purchaseYear.getText()) && !TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf(((((((Integer.parseInt(productMrp.getText().toString())) * 50) / 100) / (((Integer.parseInt(purchaseYear.getText().toString())) * 12) + (Integer.parseInt(purchaseMonth.getText().toString())))) * 10) * 50) / 100);
                productmbsp.setText(productmbspvalue + " Rs");
            } else if (damageNo.isChecked() && !TextUtils.isEmpty(purchaseYear.getText()) && TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf((((((Integer.parseInt(productMrp.getText().toString()))) / (Integer.parseInt((purchaseYear.getText().toString())) * 12)) * 10) * 50) / 100);
                productmbsp.setText(productmbspvalue + " Rs");
            } else if (damageNo.isChecked() && TextUtils.isEmpty(purchaseYear.getText()) && !TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf(((((Integer.parseInt(productMrp.getText().toString())) / (Integer.parseInt(purchaseMonth.getText().toString()))) * 10) * 50) / 100);
                productmbsp.setText(productmbspvalue + " Rs");
            } else if (damageNo.isChecked() && !TextUtils.isEmpty(purchaseYear.getText()) && !TextUtils.isEmpty(purchaseMonth.getText())) {
                productmbspvalue = String.valueOf((((((Integer.parseInt(productMrp.getText().toString())) / (((Integer.parseInt(purchaseYear.getText().toString())) * 12) + (Integer.parseInt(purchaseMonth.getText().toString())))) * 10) * 50) / 100));
                productmbsp.setText(productmbspvalue + " Rs");
            }
        } else if (TextUtils.isEmpty(productMrp.getText())) {
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.entermrp), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.error));
            snackbar.show();
        } else if (TextUtils.isEmpty(purchaseYear.getText()) && TextUtils.isEmpty(purchaseMonth.getText())) {
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.enterpurchaseperiod), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.error));
            snackbar.show();
        } else if (!damageYes.isChecked() && !damageNo.isChecked()) {
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.damagecheck), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.error));
            snackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imagedata = data.getData();
            productImage.setImageURI(imagedata);
            productImageUri = imagedata.toString();
            Log.i("IMAGE", "onActivityResult: " + productImageUri);
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        switch (arg0.getId()) {
            case R.id.catergorylistupload:
                productcategory = categorylist[position];
                break;
            case R.id.verificationlistupload:
                productverification = verificationlist[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void uploadProduct() {

        if ((imagedata != null || fetchedImage != null) && !TextUtils.isEmpty(productName.getText().toString()) && !TextUtils.isEmpty(productDes.getText().toString()) && !TextUtils.isEmpty(productmbsp.getText().toString()) && (shipYes.isChecked() || shipNo.isChecked()) && (shareYes.isChecked() || shareNo.isChecked())) {
            progressDialog = new ProgressDialog(EditOwnProduct.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploading Product...");
            progressDialog.setProgress(0);
            progressDialog.show();
            final Date today = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            final String dateToStr = format.format(today);


            if (imagedata != null) {
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(fetchedImage);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        StorageReference mySto = firebaseStorage.getReference();
                        mySto.child("Uploads").child(firebaseAuth.getCurrentUser().getUid() + productName.getText().toString() + dateToStr).putFile(imagedata)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String url = uri.toString();
                                                map = new HashMap<>();
                                                productString = product.push().getKey();
                                                map.put(Product.getName(), productName.getText().toString());
                                                map.put(Product.getDescription(), productDes.getText().toString());
                                                map.put(Product.getCategory(), productcategory);
                                                map.put(Product.getMrp(), productMrp.getText().toString());
                                                map.put(Product.getPurchaseYear(), purchaseYear.getText().toString());
                                                map.put(Product.getPurchaseMonth(), purchaseMonth.getText().toString());
                                                map.put(Product.getDamaged(), damagebool);
                                                if (damagebool) {
                                                    map.put(Product.damageReport, damageReport.getText().toString());
                                                }
                                                map.put(Product.getVerification(), productverification);
                                                map.put(Product.getMbsp(), productmbsp.getText().toString());
                                                map.put(Product.getShip(), shipbool);
                                                map.put(Product.getShareContact(), sharebool);
                                                if (sharebool) {
                                                    if (shareemail.isChecked()) {
                                                        map.put(Product.getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                    }
                                                    if (sharephone.isChecked()) {
                                                        map.put(Product.getPhone(), userPhone);
                                                    }
                                                }
                                                map.put(Product.getDate(), dateToStr);
                                                map.put(Product.getImage(), url);
                                                map.put(Product.getProductid(), productID);
                                                map.put(Product.getUserid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                product.child(productID).updateChildren(map);
                                                map.clear();
                                                progressDialog.dismiss();
                                                Snackbar snackbar = Snackbar.make(base, R.string.productuploaded, Snackbar.LENGTH_SHORT);
                                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.success));
                                                snackbar.show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Snackbar snackbar = Snackbar.make(base, R.string.productuploadfailed, Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.error));
                                snackbar.show();
                                progressDialog.dismiss();

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                                progressDialog.setProgress(currentProgress);

                            }
                        });
                    }
                });
            } else if (imagedata == null) {
                map = new HashMap<>();
                productString = product.push().getKey();
                map.put(Product.getName(), productName.getText().toString());
                map.put(Product.getDescription(), productDes.getText().toString());
                map.put(Product.getCategory(), productcategory);
                map.put(Product.getMrp(), productMrp.getText().toString());
                map.put(Product.getPurchaseYear(), purchaseYear.getText().toString());
                map.put(Product.getPurchaseMonth(), purchaseMonth.getText().toString());
                map.put(Product.getDamaged(), damagebool);
                if (damagebool) {
                    map.put(Product.damageReport, damageReport.getText().toString());
                }
                map.put(Product.getVerification(), productverification);
                map.put(Product.getMbsp(), productmbsp.getText().toString());
                map.put(Product.getShip(), shipbool);
                map.put(Product.getShareContact(), sharebool);
                if (sharebool) {
                    if (shareemail.isChecked()) {
                        map.put(Product.getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                    if (sharephone.isChecked()) {
                        map.put(Product.getPhone(), userPhone);
                    }
                } else {
                    product.child(productID).child(Product.getPhone()).removeValue();
                    product.child(productID).child(Product.getEmail()).removeValue();

                }
                map.put(Product.getDate(), dateToStr);
                map.put(Product.getImage(), fetchedImage);
                map.put(Product.getProductid(), productID);
                map.put(Product.getUserid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                product.child(productID).updateChildren(map);
                map.clear();
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(base, R.string.productuploaded, Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.success));
                snackbar.show();
            }

        } else {
            Snackbar snackbar = Snackbar.make(base, R.string.fillallfield, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditOwnProduct.this, R.color.error));
            snackbar.show();
        }
    }
}