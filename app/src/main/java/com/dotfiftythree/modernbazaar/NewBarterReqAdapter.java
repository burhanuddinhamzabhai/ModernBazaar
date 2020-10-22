package com.dotfiftythree.modernbazaar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NewBarterReqAdapter extends RecyclerView.Adapter<NewBarterReqAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<NewReqArrayList> products = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener buyerProductImageFetch, sellerProductImageFetch;
    private Context context;

    public NewBarterReqAdapter(Context context, ArrayList<NewReqArrayList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public NewBarterReqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.new_barter_req_items, parent, false);
        return new NewBarterReqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewBarterReqAdapter.ViewHolder holder, int position) {
        // initialize each element
        final NewReqArrayList fetchProductArrayList = products.get(position);
        holder.setBuyerProductID(fetchProductArrayList.getBuyerProduct());
        holder.setBuyerProduct(fetchProductArrayList.getBuyerProductImage());
        holder.setSellerProduct(fetchProductArrayList.getSellerProductImage());
        holder.setBarterID(fetchProductArrayList.getBarterID());
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView sellerProductImg, buyerProductImg;
        String buyerProductURL, sellerProductURL, productID;
        LinearLayout newReqLin;
        private String barterID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerProductImg = itemView.findViewById(R.id.newbarterreqsellerimg);
            buyerProductImg = itemView.findViewById(R.id.newbarterreqbuyerimg);
            newReqLin = itemView.findViewById(R.id.newReqLin);

            newReqLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transition = new Intent(context, BarterRequestDetail.class);
                    transition.putExtra(Product.getProductid(), productID);
                    transition.putExtra(Barter.getBarterID(), barterID);
                    context.startActivity(transition);
                    ((Activity) context).finish();
                }
            });

        }


        public void setBuyerProduct(String buyerProduct) {
            buyerProductURL = buyerProduct;

            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(buyerProductURL);
            GlideApp.with(context)
                    .load(ref)
                    .into(this.buyerProductImg);
        }

        public void setSellerProduct(String sellerProduct) {
            sellerProductURL = sellerProduct;
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(sellerProductURL);
            GlideApp.with(context)
                    .load(ref)
                    .into(this.sellerProductImg);

        }

        public void setBuyerProductID(String buyerProduct) {
            productID = buyerProduct;
        }

        public String getBarterID() {
            return barterID;
        }

        public void setBarterID(String barterID) {
            this.barterID = barterID;
        }
    }
}
