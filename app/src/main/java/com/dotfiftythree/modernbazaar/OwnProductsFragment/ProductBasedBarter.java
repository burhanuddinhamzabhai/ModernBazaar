package com.dotfiftythree.modernbazaar.OwnProductsFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.Adapters.HomeFetchProductAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.FetchProductArrayList;
import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.LoadingImage.GlideApp;
import com.dotfiftythree.modernbazaar.R;
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

public class ProductBasedBarter extends AppCompatActivity {
    public static String productID;
    public static String sellerImage;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productDetails;
    ImageView productImage;
    TextView productName, productMBSP;
    LinearLayout back, base;
    ChildEventListener productfetch;
    FirebaseDatabase products = FirebaseDatabase.getInstance();
    DatabaseReference productsfetch = products.getReference("ProductsDB");
    ArrayList<FetchProductArrayList> fetchProductArrayLists = new ArrayList<>();
    HomeFetchProductAdapter fetchProductAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_based_barter);
        productImage = findViewById(R.id.homedetailproductimage);
        productName = findViewById(R.id.homedetailproductname);
        productMBSP = findViewById(R.id.homedetailproductperiod);
        back = findViewById(R.id.back);
        base = findViewById(R.id.base);
        recyclerView = findViewById(R.id.homeproductlist);
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
                    GlideApp.with(ProductBasedBarter.this)
                            .load(ref)
                            .into(productImage);
                    productName.setText(_childValue.get(Product.getName()).toString());
                    productMBSP.setText(_childValue.get(Product.getMbsp()).toString());

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


        //fetching products
        productfetch = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String productName = "", productDes = "", periodOfUsage = "", productImage = "", productID = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                int value = Integer.parseInt((_childValue.get(Product.getMbsp()).toString()).replace(" Rs", ""));
                if (!((_childValue.get(Product.getUserid()).toString()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                    if (!(snapshot.child(Product.getBarter()).exists())) {
                        if ((value + 10) > Integer.parseInt(productMBSP.getText().toString().replace(" Rs", ""))) {
                            productName = _childValue.get(Product.getName()).toString();
                            productDes = _childValue.get(Product.getDescription()).toString();
                            if (_childValue.get(Product.getPurchaseYear()).equals("")) {
                                periodOfUsage = _childValue.get(Product.getPurchaseMonth()).toString() + " Months";
                            } else if (_childValue.get(Product.getPurchaseMonth()).equals("")) {
                                periodOfUsage = _childValue.get(Product.getPurchaseYear()).toString() + " Years";
                            } else {
                                periodOfUsage = _childValue.get(Product.getPurchaseYear()).toString() + " Years " + _childValue.get(Product.getPurchaseMonth()).toString() + " Months";
                            }
                            productImage = _childValue.get(Product.getImage()).toString();
                            Log.i("TAG", "onChildAdded: " + productImage);
                            productID = _childValue.get(Product.getProductid()).toString();

                            fetchProductArrayLists.add(new FetchProductArrayList(productImage, productName, periodOfUsage, productDes, productID));
                            fetchProductAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.noproductfound), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(ProductBasedBarter.this, R.color.red));
                    snackbar.show();


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
        productsfetch.addChildEventListener(productfetch);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductBasedBarter.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        fetchProductAdapter = new HomeFetchProductAdapter(ProductBasedBarter.this, fetchProductArrayLists);
        recyclerView.setAdapter(fetchProductAdapter);


    }
}