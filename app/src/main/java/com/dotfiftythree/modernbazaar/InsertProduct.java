package com.dotfiftythree.modernbazaar;

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

public class InsertProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int GALLERY_REQUEST_CODE = 123;
    String[] categorylist = {"Laptop", "Computer", "Printer", "Scanner", "Mobile", "Tablet", "Camera",
            "Tv", "Fridge", "Wall AC", "Tower AC", "Cooler", "Biker", "Car", "SUV", "Oven", "Sport Car", "Sport Bike",
            "Ricksaw", "Furniture"};
    String[] verificationlist = {"Request", "Unverified"};
    private LinearLayout back, selectImage, damagereportlin, sharelin, base;
    private ImageView productImage;
    private RadioGroup damage, shareContactGrp, shipping;
    private RadioButton damageYes, damageNo, shareYes, shareNo, shipYes, shipNo;
    private ScrollView productDetails;
    private Button calculatembsp, upload, cancel;
    private CheckBox shareemail, sharephone;
    String productImageUri, productcategory, productverification, productmbspvalue;
    private Spinner categoryspin, verificationspin;
    private EditText productName, productDes, productMrp, purchaseYear, purchaseMonth, damageReport, productmbsp;
    private Uri imagedata;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ProgressDialog progressDialog;
    private DatabaseReference userDB = firebaseDatabase.getReference("userDB");
    DatabaseReference product = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productListener, userListener;
    private HashMap<String, Object> map = new HashMap<>();
    private String productString = "", userName, userPhone;
    private boolean damagebool, shipbool, sharebool, emailbool, phonebool;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);
        back = findViewById(R.id.backforinsert);
        selectImage = findViewById(R.id.changeimage);
        productImage = findViewById(R.id.uploadproductimage);
        productName = findViewById(R.id.productnameupload);
        productDes = findViewById(R.id.productdesupload);
        purchaseYear = findViewById(R.id.productbuyyearupload);
        purchaseMonth = findViewById(R.id.productbuymonthupload);
        productMrp = findViewById(R.id.productmrpupload);
        damageReport = findViewById(R.id.damagedesupload);
        productmbsp = findViewById(R.id.productmbspupload);
        damage = findViewById(R.id.damagegroupupload);
        damageYes = findViewById(R.id.damageyesupload);
        damageNo = findViewById(R.id.damagenoupload);
        damagereportlin = findViewById(R.id.damagereportlin);
        sharelin = findViewById(R.id.sharecontactuploadlin);
        shareContactGrp = findViewById(R.id.sharegroupupload);
        shareYes = findViewById(R.id.shareyesupload);
        shareNo = findViewById(R.id.sharenoupload);
        shipping = findViewById(R.id.shipgroupupload);
        shipYes = findViewById(R.id.shipyesupload);
        shipNo = findViewById(R.id.shipnoupload);
        base = findViewById(R.id.base);
        shareemail = findViewById(R.id.shareemailupload);
        sharephone = findViewById(R.id.sharephoneupload);
        calculatembsp = findViewById(R.id.calculatembspupload);
        categoryspin = findViewById(R.id.catergorylistupload);
        verificationspin = findViewById(R.id.verificationlistupload);
        upload = findViewById(R.id.uploadproductbtn);
        cancel = findViewById(R.id.cancelproductuploadbtn);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner categoryspin = (Spinner) findViewById(R.id.catergorylistupload);
        categoryspin.setOnItemSelectedListener(this);
        Spinner verificationspin = (Spinner) findViewById(R.id.verificationlistupload);
        verificationspin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorylist);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        categoryspin.setAdapter(a1);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, verificationlist);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        verificationspin.setAdapter(a2);

//fetching current user data
        userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String Name = "",Phone = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                if (_childKey.equals(firebaseAuth.getCurrentUser().getUid())) {
                    Name = _childValue.get(User.getName()).toString();
                    Phone = _childValue.get(User.getPhone()).toString();
                    Log.i("name", "onChildAdded: "+userName);
                    Log.i("num", "onChildAdded: "+userPhone);
                    userName=Name;
                    userPhone=Phone;
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
                    case R.id.damageyesupload:
                        damagereportlin.setVisibility(View.VISIBLE);
                        damagebool = true;
                        break;
                    case R.id.damagenoupload:
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
                    case R.id.shareyesupload:
                        sharebool = true;
                        sharelin.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.swipeup), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.blue));
                        snackbar.show();
                        break;
                    case R.id.sharenoupload:
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
                    case R.id.shipyesupload:
                        shipbool = true;
                        break;
                    case R.id.shipnoupload:
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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProduct();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        } else if (TextUtils.isEmpty(purchaseYear.getText()) && TextUtils.isEmpty(purchaseMonth.getText())) {
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.enterpurchaseperiod), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        } else if (!damageYes.isChecked() && !damageNo.isChecked()) {
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.damagecheck), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
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
        // TODO Auto-generated method stub
    }

    private void uploadProduct() {
        //tODO: CONDITION FOR DAMAGE REPORT AND EMAIL AND PHONE
        if (imagedata != null && !TextUtils.isEmpty(productName.getText().toString()) && !TextUtils.isEmpty(productDes.getText().toString()) && !TextUtils.isEmpty(productmbsp.getText().toString()) && (shipYes.isChecked() || shipNo.isChecked()) && (shareYes.isChecked() || shareNo.isChecked()) ) {
            progressDialog = new ProgressDialog(InsertProduct.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploading Product...");
            progressDialog.setProgress(0);
            progressDialog.show();
            final Date today = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            final String dateToStr = format.format(today);
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
                                    map.put(Product.getProductid(), firebaseAuth.getCurrentUser().getUid() + productName.getText().toString() + dateToStr);
                                    map.put(Product.getUserid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    product.child(firebaseAuth.getCurrentUser().getUid() + productName.getText().toString() + dateToStr).updateChildren(map);
                                    map.clear();
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(base, R.string.productuploaded, Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.success));
                                    snackbar.show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Snackbar snackbar = Snackbar.make(base, R.string.productuploadfailed, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
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

        }else{
            Snackbar snackbar = Snackbar.make(base, R.string.fillallfield, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        }
    }
}