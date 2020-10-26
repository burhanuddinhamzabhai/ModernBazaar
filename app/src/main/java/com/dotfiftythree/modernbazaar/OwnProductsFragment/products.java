package com.dotfiftythree.modernbazaar.OwnProductsFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dotfiftythree.modernbazaar.Adapters.OwnProductsAdapter;
import com.dotfiftythree.modernbazaar.ArrayLists.OwnProductsList;
import com.dotfiftythree.modernbazaar.Constants.Product;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class products extends Fragment {

    RecyclerView ownProductRecyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userProducts = firebaseDatabase.getReference("ProductsDB");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (mContext != null) {
                final int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle(R.string.Attention);
                alertDialog.setMessage(R.string.deletethis);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(fetchProductArrayLists.get(position).getProductImage());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                userProducts.child(fetchProductArrayLists.get(position).getProductID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        final Snackbar snackbar = Snackbar.make(ownProductRecyclerView, R.string.productdeleted, Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.error));
                                        snackbar.show();
                                    }
                                });
                            }
                        });

                    }
                });
                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Snackbar snackbar = Snackbar.make(ownProductRecyclerView, R.string.productwillbeaddedbacksoon, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                        snackbar.show();
                    }
                });
                alertDialog.show();
            }
        }
    };

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(ownProductRecyclerView);

        return v;


    }
}