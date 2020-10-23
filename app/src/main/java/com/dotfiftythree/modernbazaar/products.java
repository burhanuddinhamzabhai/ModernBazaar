package com.dotfiftythree.modernbazaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;

public class products extends Fragment {


    RecyclerView ownProductRecyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userProducts = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productsFetcher;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    OwnProductsAdapter ownProductsAdapter;
    ArrayList<OwnProductsList> fetchProductArrayLists = new ArrayList<>();
    ProgressDialog progressDialog;
    private Context mContext;

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

    public products() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ownProductRecyclerView = v.findViewById(R.id.ownProductsRecyclerView);
        productsFetcher = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String productID = "", productName = "", productImage = "";
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childValue.get(Product.getUserid()).equals(mAuth.getCurrentUser().getUid())) {
                    if (mContext != null) {
                        progressDialog = new ProgressDialog(mContext);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setTitle("Loading Product...");
                        progressDialog.show();
                        productID = _childValue.get(Product.getProductid()).toString();
                        productName = _childValue.get(Product.getName()).toString();
                        productImage = _childValue.get(Product.getImage()).toString();
                        fetchProductArrayLists.add(new OwnProductsList(productID, productName, productImage));
                        ownProductsAdapter.notifyDataSetChanged();
                        progressDialog.cancel();
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
        userProducts.addChildEventListener(productsFetcher);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        ownProductRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        ownProductsAdapter = new OwnProductsAdapter(getActivity(), fetchProductArrayLists);
        ownProductRecyclerView.setAdapter(ownProductsAdapter);

        return v;
    }
}