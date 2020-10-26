package com.dotfiftythree.modernbazaar.HomeFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.Adapters.HomeFetchProductAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.FetchProductArrayList;
import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;


public class home extends Fragment {

    SeekBar mbsppb;
    ChildEventListener maxminmbsp, productfetch, changefetch;
    FirebaseDatabase products = FirebaseDatabase.getInstance();
    DatabaseReference productsfetch = products.getReference("ProductsDB");
    ArrayList<FetchProductArrayList> fetchProductArrayLists = new ArrayList<>();
    HomeFetchProductAdapter fetchProductAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private LinearLayout mbspdes, base;
    ArrayList<Integer> mbspvalues = new ArrayList<>();
    int min, max;
    private TextView minmbsp;
    private TextView maxmbsp;
    private TextView currentmbsp;
    private Context mContext;
    String bundleMBSP;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView mbspinfo = v.findViewById(R.id.homembspinfo);
        mbspdes = v.findViewById(R.id.homembspdetail);
        base = v.findViewById(R.id.base);
        mbsppb = v.findViewById(R.id.setmbsphome);
        minmbsp = v.findViewById(R.id.homeminmbsp);
        currentmbsp = v.findViewById(R.id.homecurrentmbsp);
        maxmbsp = v.findViewById(R.id.homemaxmbsp);
        recyclerView = v.findViewById(R.id.homeproductlist);
        mbspinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbspdes.getVisibility() == View.GONE) {
                    mbspdes.setVisibility(View.VISIBLE);
                    if (mContext != null) {
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.mbspclickagain), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        snackbar.show();
                    }
                } else {
                    mbspdes.setVisibility(View.GONE);
                }
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading Products...");
        progressDialog.setProgress(0);
        progressDialog.show();
        maxminmbsp = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int value;
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (!((_childValue.get(Product.getUserid()).toString()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                    if (!(snapshot.child(Product.getBarter()).exists())) {
                        value = Integer.parseInt((Objects.requireNonNull(_childValue.get(Product.getMbsp())).toString()).replace(" Rs", ""));
                        mbspvalues.add(value);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mbsppb.setMin(Collections.min(mbspvalues));
                            min = Collections.min(mbspvalues);
                        }
                        mbsppb.setMax(Collections.max(mbspvalues));
                        max = Collections.max(mbspvalues);
                        Log.i("minmax", "min" + Collections.min(mbspvalues) + "max" + Collections.max(mbspvalues));


                    }
//                Query maxvalue = productsfetch.orderByChild(Product.getMbsp());
//                maxvalue.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            String max = childSnapshot.child(Product.getMbsp()).getValue().toString();
//                            Toast.makeText(getActivity(),max,Toast.LENGTH_SHORT).show();
//                            int maxv = Integer.parseInt(max.replace(" Rs", ""));
//                            mbsppb.setMax(maxv);
//                            maxmbsp.setText(max);
//                            Query minvalue = productsfetch.orderByChild(Product.getMbsp()).limitToLast(1);
//                            minvalue.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                        String min = childSnapshot.child(Product.getMbsp()).getValue().toString();
//
//                                        int minv = Integer.parseInt(min.replace(" Rs", ""));
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            mbsppb.setMin(minv);
//                                        }
//                                        minmbsp.setText(min);
//
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    throw databaseError.toException(); // don't swallow errors
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        throw databaseError.toException(); // don't swallow errors
//                    }
//                });
                } else {
                    progressDialog.cancel();
                    if (mContext != null) {
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.noproductfound), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        snackbar.show();
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
                if (!((_childValue.get(Product.getUserid()).toString()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                    if (!(snapshot.child(Product.getBarter()).exists())) {
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
                        minmbsp.setText(min + " Rs");
                        maxmbsp.setText(max + " Rs");
                    }
                } else {
                    progressDialog.cancel();
                    if (mContext != null) {
                        Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.noproductfound), Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        snackbar.show();
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
        productsfetch.addChildEventListener(productfetch);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        fetchProductAdapter = new HomeFetchProductAdapter(getActivity(), fetchProductArrayLists);
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
                            if (!((_childValue.get(Product.getUserid()).toString()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                                if ((progress + 10) > value && (!(snapshot.child(Product.getBarter())).exists())) {
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

                            } else {
                                progressDialog.cancel();
                                if (mContext != null) {
                                    Snackbar snackbar = Snackbar.make(base, getResources().getString(R.string.noproductfound), Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                                    snackbar.show();
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


        // Inflate the layout for this fragment
        return v;
    }
}