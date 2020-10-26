package com.dotfiftythree.modernbazaar.ProfileFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.Adapters.HistoryBarterRequestAdapter;
import com.dotfiftythree.modernbazaar.Adapters.OngoingBarterRequestAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.HistoryBarterRequestList;
import com.dotfiftythree.modernbazaar.ArrayLists.OngoingBarterRequestList;
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

public class RecentBarterRequests extends AppCompatActivity {
    RecyclerView ongoingRecyclerView, historyRecyclerView;
    ChildEventListener ongoingProductFetch, historyProductFetch;
    DatabaseReference productsfetch = FirebaseDatabase.getInstance().getReference().child("BarterDB");
    ArrayList<HistoryBarterRequestList> historyBarterRequestLists = new ArrayList<>();
    ArrayList<OngoingBarterRequestList> ongoingBarterRequestLists = new ArrayList<>();
    OngoingBarterRequestAdapter ongoingBarterRequestAdapter;
    HistoryBarterRequestAdapter historyBarterRequestAdapter;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_barter_requests);
        ongoingRecyclerView = findViewById(R.id.ongoinbarterrecycler);
        historyRecyclerView = findViewById(R.id.historybarterrecycler);
        back = findViewById(R.id.backfromrecentbarter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //fetching products
        ongoingProductFetch = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String buyer = "", seller = "", buyerProduct = "", sellerProduct = "", date = "", mobile = "", mail = "", barterID = "", buyerProductImg = "", sellerProductImg = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (!(snapshot.child(Barter.getBarterResponse()).exists())) {
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


                    ongoingBarterRequestLists.add(new OngoingBarterRequestList(buyer, seller, buyerProduct, sellerProduct, date, mobile, mail, barterID, buyerProductImg, sellerProductImg));
                    ongoingBarterRequestAdapter.notifyDataSetChanged();
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
        productsfetch.addChildEventListener(ongoingProductFetch);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ongoingRecyclerView.setLayoutManager(layoutManager);
        ongoingBarterRequestAdapter = new OngoingBarterRequestAdapter(RecentBarterRequests.this, ongoingBarterRequestLists);
        ongoingRecyclerView.setAdapter(ongoingBarterRequestAdapter);

        //fetching products
        historyProductFetch = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String buyer = "", seller = "", buyerProduct = "", sellerProduct = "", date = "", mobile = "", mail = "", barterID = "", buyerProductImg = "", sellerProductImg = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (snapshot.child(Barter.getBarterResponse()).exists()) {
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


                    historyBarterRequestLists.add(new HistoryBarterRequestList(buyer, seller, buyerProduct, sellerProduct, date, mobile, mail, barterID, buyerProductImg, sellerProductImg));
                    historyBarterRequestAdapter.notifyDataSetChanged();
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
        productsfetch.addChildEventListener(historyProductFetch);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(layoutManager1);
        historyBarterRequestAdapter = new HistoryBarterRequestAdapter(RecentBarterRequests.this, historyBarterRequestLists);
        historyRecyclerView.setAdapter(historyBarterRequestAdapter);

    }
}
