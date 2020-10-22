package com.dotfiftythree.modernbazaar;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryBarterRequestAdapter extends RecyclerView.Adapter<HistoryBarterRequestAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<HistoryBarterRequestList> products = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("BarterDB");
    HashMap<String, Object> map = new HashMap<>();
    private Context context;

    public HistoryBarterRequestAdapter(Context context, ArrayList<HistoryBarterRequestList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public HistoryBarterRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.history_barter_item, parent, false);
        return new HistoryBarterRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBarterRequestAdapter.ViewHolder holder, int position) {
        // initialize each element
        final HistoryBarterRequestList fetchProductArrayList = products.get(position);
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
        LinearLayout newReqLin, copybarterid;
        private String barterID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerProductImg = itemView.findViewById(R.id.historybartersellerimg);
            buyerProductImg = itemView.findViewById(R.id.historybarterbuyerimg);
            newReqLin = itemView.findViewById(R.id.historybarternewReqLin);
            copybarterid = itemView.findViewById(R.id.historybartercopybarterid);
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
            copybarterid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("BarterID", barterID);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, R.string.barteridcopied, Toast.LENGTH_SHORT).show();
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
