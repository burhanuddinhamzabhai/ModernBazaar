package com.dotfiftythree.modernbazaar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Visitor extends AppCompatActivity {
    SeekBar mbsppb;
    ChildEventListener maxminmbsp, productfetch, changefetch;
    DatabaseReference productsfetch = FirebaseDatabase.getInstance().getReference().child("ProductsDB");
    ArrayList<FetchProductArrayList> fetchProductArrayLists = new ArrayList<>();
    FetchProductAdapter fetchProductAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private LinearLayout mbspdes, base;
    private TextView mbspinfo, signup, minmbsp, maxmbsp, currentmbsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        mbspinfo = findViewById(R.id.visitormbspinfo);
        mbspdes = findViewById(R.id.visitormbspdetail);
        signup = findViewById(R.id.visitorsignup);
        base = findViewById(R.id.base);
        mbsppb = findViewById(R.id.setmbspvisitor);
        minmbsp = findViewById(R.id.visitorminmbsp);
        currentmbsp = findViewById(R.id.visitorcurrentmbsp);
        maxmbsp = findViewById(R.id.visitormaxmbsp);
        recyclerView = findViewById(R.id.visitorproductlist);
        mbspinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbspdes.getVisibility() == View.GONE) {
                    mbspdes.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.mbspclickagain), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(Visitor.this, R.color.blue));
                    snackbar.show();
                } else {
                    mbspdes.setVisibility(View.GONE);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(Visitor.this, Registration.class);
                startActivity(transition);
                finish();
            }
        });
        progressDialog = new ProgressDialog(Visitor.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading Products...");
        progressDialog.setProgress(0);
        progressDialog.show();
        maxminmbsp = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                Query maxvalue = productsfetch.orderByChild(Product.getMbsp()).limitToFirst(1);
                maxvalue.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String max = childSnapshot.child(Product.getMbsp()).getValue().toString();

                            int maxv = Integer.parseInt(max.replace(" Rs", ""));
                            mbsppb.setMax(maxv);
                            maxmbsp.setText(max);
                            Query minvalue = productsfetch.orderByChild(Product.getMbsp()).limitToLast(1);
                            minvalue.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String min = childSnapshot.child(Product.getMbsp()).getValue().toString();

                                        int minv = Integer.parseInt(min.replace(" Rs", ""));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            mbsppb.setMin(minv);
                                        }
                                        minmbsp.setText(min);


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't swallow errors
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException(); // don't swallow errors
                    }
                });
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
        productsfetch.addChildEventListener(maxminmbsp);

        //fetching products
        productfetch = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String productName = "", productDes = "", periodOfUsage = "", productImage = "", productID = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
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
                progressDialog.cancel();
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        fetchProductAdapter = new FetchProductAdapter(Visitor.this, fetchProductArrayLists);
        recyclerView.setAdapter(fetchProductAdapter);


        mbsppb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (fromUser) {
                    fetchProductArrayLists.clear();
                    currentmbsp.setText(progress + " Rs");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mbsppb.setMin(Integer.parseInt((minmbsp.getText().toString()).replace(" Rs", "")));
                    }
                    changefetch = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String productName = "", productDes = "", periodOfUsage = "", productImage = "", productID = "";
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            final String _childKey = snapshot.getKey();
                            final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                            int value = Integer.parseInt((_childValue.get(Product.getMbsp()).toString()).replace(" Rs", ""));
                            Log.i("TAG2", "onChildAdded: " + value);
                            if ((progress + 10) > value) {
                                recyclerView.setVisibility(View.VISIBLE);
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
                    productsfetch.addChildEventListener(changefetch);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}