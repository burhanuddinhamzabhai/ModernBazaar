package com.dotfiftythree.modernbazaar.ProfileFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.Adapters.NewBarterReqAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.NewReqArrayList;
import com.dotfiftythree.modernbazaar.Constants.Barter;
import com.dotfiftythree.modernbazaar.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;

public class NewBarterRequest extends AppCompatActivity {

    ChildEventListener productfetch;
    DatabaseReference productsfetch = FirebaseDatabase.getInstance().getReference().child("BarterDB");
    ArrayList<NewReqArrayList> fetchProductArrayLists = new ArrayList<>();
    NewBarterReqAdapter barterProductListAdapter;
    RecyclerView recyclerView;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_barter_request);
        recyclerView = findViewById(R.id.newbarterrequestrecyclerview);
        back = findViewById(R.id.backfromnewreq);
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
                String buyer = "", seller = "", buyerProduct = "", sellerProduct = "", date = "", mobile = "", mail = "", barterID = "", buyerProductImg = "", sellerProductImg = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (!(snapshot.child(Barter.getRequestSeen()).exists())) {
                    buyer = _childValue.get(Barter.getBuyer()).toString();
                    seller = _childValue.get(Barter.getSeller()).toString();
                    buyerProduct = _childValue.get(Barter.getBuyerProduct()).toString();
                    sellerProduct = _childValue.get(Barter.getSellerProduct()).toString();
                    date = _childValue.get(Barter.getDate()).toString();
                    if (snapshot.child(Barter.getBuyerMobile()).exists()) {
                        mobile = _childValue.get(Barter.getBuyerMobile()).toString();
                    }
                    if (snapshot.child(Barter.getBuyerMail()).exists()) {
                        mail = _childValue.get(Barter.getBuyerMail()).toString();
                    }
                    barterID = _childValue.get(Barter.getBarterID()).toString();
                    buyerProductImg = _childValue.get(Barter.getBuyerProductImg()).toString();
                    sellerProductImg = _childValue.get(Barter.getSellerProductImg()).toString();


                    fetchProductArrayLists.add(new NewReqArrayList(buyer, seller, buyerProduct, sellerProduct, date, mobile, mail, barterID, buyerProductImg, sellerProductImg));
                    barterProductListAdapter.notifyDataSetChanged();
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        barterProductListAdapter = new NewBarterReqAdapter(NewBarterRequest.this, fetchProductArrayLists);
        recyclerView.setAdapter(barterProductListAdapter);

    }
}