package com.dotfiftythree.modernbazaar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class InsertProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final int GALLERY_REQUEST_CODE=123;
    String[] categorylist = { "Laptop","Computer","Printer","Scanner","Mobile","Tablet","Camera",
                        "Tv","Fridge","Wall AC","Tower AC","Cooler","Biker","Car","SUV","Oven","Sport Car","Sport Bike",
                        "Ricksaw","Furniture"};
    String[] verificationlist ={ "Request", "Unverified"};
    private LinearLayout back,selectImage,damagereportlin,sharelin,base;
    private ImageView productImage;
    private RadioGroup damage,shareContactGrp,shipping;
    private RadioButton damageYes,damageNo,shareYes,shareNo,shipYes,shipNo;
    private ScrollView productDetails;
    private Button calculatembsp;
    private CheckBox shareemail,sharephone;
    String productImageUri, productcategory,productverification,productmbspvalue;
    private Spinner categoryspin,verificationspin;
    private EditText productName, productDes, productMrp,purchaseYear, purchaseMonth,damageReport,productmbsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);
        back = findViewById(R.id.backforinsert);
        selectImage=findViewById(R.id.changeimage);
        productImage=findViewById(R.id.uploadproductimage);
        productName=findViewById(R.id.productnameupload);
        productDes=findViewById(R.id.productdesupload);
        purchaseYear=findViewById(R.id.productbuyyearupload);
        purchaseMonth=findViewById(R.id.productbuymonthupload);
        productMrp=findViewById(R.id.productmrpupload);
        damageReport=findViewById(R.id.damagedesupload);
        productmbsp=findViewById(R.id.productmbspupload);
        damage = findViewById(R.id.damagegroupupload);
        damageYes = findViewById(R.id.damageyesupload);
        damageNo = findViewById(R.id.damagenoupload);
        damagereportlin=findViewById(R.id.damagereportlin);
        sharelin=findViewById(R.id.sharecontactuploadlin);
        shareContactGrp=findViewById(R.id.sharegroupupload);
        shareYes=findViewById(R.id.shareyesupload);
        shareNo=findViewById(R.id.sharenoupload);
        shipping=findViewById(R.id.shipgroupupload);
        shipYes=findViewById(R.id.shipyesupload);
        shipNo=findViewById(R.id.shipnoupload);
        base=findViewById(R.id.base);
        shareemail=findViewById(R.id.shareemailupload);
        sharephone=findViewById(R.id.sharephoneupload);
        calculatembsp=findViewById(R.id.calculatembspupload);
        categoryspin=findViewById(R.id.catergorylistupload);
        verificationspin=findViewById(R.id.verificationlistupload);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner categoryspin = (Spinner) findViewById(R.id.catergorylistupload);
        categoryspin.setOnItemSelectedListener(this);
        Spinner verificationspin = (Spinner) findViewById(R.id.verificationlistupload);
        verificationspin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categorylist);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        categoryspin.setAdapter(a1);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,verificationlist);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        verificationspin.setAdapter(a2);


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
                startActivityForResult(Intent.createChooser(pickImage,"Pick Product Image"),GALLERY_REQUEST_CODE);

            }
        });
        //checking damage selected by user
        damage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.damageyesupload:
                        damagereportlin.setVisibility(View.VISIBLE);
                        break;
                    case R.id.damagenoupload:
                        damagereportlin.setVisibility(View.GONE);
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
                        sharelin.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.swipeup), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.blue));
                        snackbar.show();
                        break;
                    case R.id.sharenoupload:
                        //TODO: uncheck both checkboxes
                        sharelin.setVisibility(View.GONE);
                        shareemail.setChecked(false);
                        sharephone.setChecked(false);
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
    }

    private void mbspCalculate() {
        if(!TextUtils.isEmpty(productMrp.getText())&&(!TextUtils.isEmpty(purchaseYear.getText())||!TextUtils.isEmpty(purchaseMonth.getText()))&&(damageYes.isChecked()||damageNo.isChecked())){
           if(damageYes.isChecked()&&!TextUtils.isEmpty(purchaseYear.getText())&&TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf(((((((Integer.parseInt(productMrp.getText().toString()))*50)/100)/(Integer.parseInt((purchaseYear.getText().toString()))*12))*10)*50)/100);
                productmbsp.setText(productmbspvalue+" Rs");
           }else if(damageYes.isChecked()&&TextUtils.isEmpty(purchaseYear.getText())&&!TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf(((((((Integer.parseInt(productMrp.getText().toString()))*50)/100)/(Integer.parseInt(purchaseMonth.getText().toString())))*10)*50)/100);
               productmbsp.setText(productmbspvalue+" Rs");
           }else if(damageYes.isChecked()&&!TextUtils.isEmpty(purchaseYear.getText())&&!TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf(((((((Integer.parseInt(productMrp.getText().toString()))*50)/100)/(((Integer.parseInt(purchaseYear.getText().toString()))*12)+(Integer.parseInt(purchaseMonth.getText().toString()))))*10)*50)/100);
               productmbsp.setText(productmbspvalue+" Rs");
           }else if(damageNo.isChecked()&&!TextUtils.isEmpty(purchaseYear.getText())&&TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf((((((Integer.parseInt(productMrp.getText().toString())))/(Integer.parseInt((purchaseYear.getText().toString()))*12))*10)*50)/100);
               productmbsp.setText(productmbspvalue+" Rs");
           }else if(damageNo.isChecked()&&TextUtils.isEmpty(purchaseYear.getText())&&!TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf(((((Integer.parseInt(productMrp.getText().toString()))/(Integer.parseInt(purchaseMonth.getText().toString())))*10)*50)/100);
               productmbsp.setText(productmbspvalue+" Rs");
           }else if(damageNo.isChecked()&&!TextUtils.isEmpty(purchaseYear.getText())&&!TextUtils.isEmpty(purchaseMonth.getText())){
               productmbspvalue=String.valueOf((((((Integer.parseInt(productMrp.getText().toString()))/(((Integer.parseInt(purchaseYear.getText().toString()))*12)+(Integer.parseInt(purchaseMonth.getText().toString()))))*10)*50)/100));
               productmbsp.setText(productmbspvalue+" Rs");
           }

        }else if (TextUtils.isEmpty(productMrp.getText())){
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.entermrp), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        }else if (TextUtils.isEmpty(purchaseYear.getText())&&TextUtils.isEmpty(purchaseMonth.getText())){
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.enterpurchaseperiod), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        }else if (!damageYes.isChecked()&&!damageNo.isChecked()){
            Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.damagecheck), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(InsertProduct.this, R.color.error));
            snackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imagedata = data.getData();
            productImage.setImageURI(imagedata);
            productImageUri=imagedata.toString();
            Log.i("IMAGE", "onActivityResult: "+productImageUri);
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        switch (arg0.getId()) {
            case R.id.catergorylistupload:
            productcategory=categorylist[position];
            break;
            case R.id.verificationlistupload:
                productverification=verificationlist[position];
                break;
            default:
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}