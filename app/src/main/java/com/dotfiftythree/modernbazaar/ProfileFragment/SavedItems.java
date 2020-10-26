package com.dotfiftythree.modernbazaar.ProfileFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.Adapters.SavedItemsAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.SavedItemsList;
import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.Constants.User;
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
    HashMap<String, Object> map = new HashMap<>();
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final String newSavedString, modifiedString = savedProducts, old;
            old = savedItemsLists.get(position).productID + ", ";
            newSavedString = modifiedString.replace(savedItemsLists.get(position).productID + ", ", "");
            Log.i("TAG", "onSwiped: " + newSavedString);
            Log.i("TAG", "onSwiped: " + savedProducts);
            Log.i("TAG", "onSwiped: " + modifiedString);

            map = new HashMap<>();
            map.put(User.getSavedItems(), newSavedString);
            userDatabase.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    map.clear();
                    savedItemsLists.remove(position);
                    savedItemsAdapter.notifyItemRemoved(position);
                    final Snackbar snackbar = Snackbar.make(savedItemRecyclerView, R.string.itemreoved, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(SavedItems.this, R.color.error));
                    snackbar.show();
                }
            });
        }
    };

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(savedItemRecyclerView);


    }
}

