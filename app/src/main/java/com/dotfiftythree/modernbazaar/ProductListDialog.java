package com.dotfiftythree.modernbazaar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
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

public class ProductListDialog extends AppCompatDialogFragment {
    ArrayList<BarterProductList> barterProductLists = new ArrayList<>();
    BarterProductListAdapter barterProductListAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener userItemFetchListener;
    private ProductListDialogListner listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.barter_product_list, null);
        recyclerView = view.findViewById(R.id.productlistbarter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        barterProductListAdapter = new BarterProductListAdapter(getActivity(), barterProductLists);
        recyclerView.setAdapter(barterProductListAdapter);
        builder.setView(view)
                .setTitle("Select a product to barter with")
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        userItemFetchListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String productName, productID;
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.contains(mAuth.getCurrentUser().getUid())) {

                    productName = _childValue.get(Product.getName()).toString();
                    productID = _childValue.get(Product.getProductid()).toString();
                    Log.i("TAG", "onChildAdded: " + productName);
                    barterProductLists.add(new BarterProductList(productName, productID));
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
        databaseReference.addChildEventListener(userItemFetchListener);

        return builder.create();
    }

    public interface ProductListDialogListner {

    }

}