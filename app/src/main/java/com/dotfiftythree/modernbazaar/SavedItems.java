package com.dotfiftythree.modernbazaar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedItems extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userDatabase = firebaseDatabase.getReference("userDB");
    DatabaseReference productDatabase = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener userDB, productDB;
    ArrayList<SavedItemsList> savedItemsLists = new ArrayList<>();
    SavedItemsAdapter savedItemsAdapter;
    RecyclerView savedItemRecyclerView;
    LinearLayout back;
    String savedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        back = findViewById(R.id.backfromsaveditems);
        savedItemRecyclerView = findViewById(R.id.saveditemsrecyclerview);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        userDB = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String saved;
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(mAuth.getCurrentUser().getUid())) {
                    if (snapshot.child(User.getSavedItems()).exists()) {
                        saved = _childValue.get(User.getSavedItems()).toString();
                        savedProducts = saved;
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
        userDatabase.addChildEventListener(userDB);


        productDB = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("saved", savedProducts);
                String productName = "", productImage = "", productID = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (savedProducts.contains(_childValue.get(Product.getProductid()).toString())) {
                    productID = _childValue.get(Product.getProductid()).toString();
                    productName = _childValue.get(Product.getName()).toString();
                    productImage = _childValue.get(Product.getImage()).toString();
                    Log.i("pname", productName);
                    savedItemsLists.add(new SavedItemsList(productName, productImage, productID));
                    savedItemsAdapter.notifyDataSetChanged();
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
        productDatabase.addChildEventListener(productDB);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SavedItems.this);
        savedItemRecyclerView.setLayoutManager(linearLayoutManager);
        savedItemsAdapter = new SavedItemsAdapter(SavedItems.this, savedItemsLists);
        savedItemRecyclerView.setAdapter(savedItemsAdapter);

    }
}