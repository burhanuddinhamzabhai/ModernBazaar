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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SavedItemsAdapter extends RecyclerView.Adapter<SavedItemsAdapter.ViewHolder> {
    ArrayList<SavedItemsList> products = new ArrayList<>();
    private Context context;

    public SavedItemsAdapter(Context context, ArrayList<SavedItemsList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public SavedItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.saved_items_item, parent, false);
        return new SavedItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedItemsAdapter.ViewHolder holder, int position) {
        // initialize each element
        final SavedItemsList fetchProductArrayList = products.get(position);
        holder.setProductName(fetchProductArrayList.getProductName());
        holder.setProductImage(fetchProductArrayList.getProductImage());
        holder.setProductID(fetchProductArrayList.getProductID());
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView sellerProductImg;
        String productName, sellerProductURL, productID;
        LinearLayout savedItemLin, copyproductid;
        TextView productNametxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerProductImg = itemView.findViewById(R.id.saveditemsellerimg);
            productNametxt = itemView.findViewById(R.id.saveditemproductname);
            savedItemLin = itemView.findViewById(R.id.saveditemsitemLin);
            copyproductid = itemView.findViewById(R.id.saveditemcopyproductid);


            savedItemLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transition = new Intent(context, HomeViewInDetail.class);
                    transition.putExtra(Product.getProductid(), productID);
                    context.startActivity(transition);
                    ((Activity) context).finish();
                }
            });

            copyproductid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("ProductID", productID);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, R.string.pidcopy, Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void setProductName(String productName) {
            this.productName = productName;
            productNametxt.setText(productName);
        }


        public void setProductImage(String productImage) {
            sellerProductURL = productImage;
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(sellerProductURL);
            GlideApp.with(context)
                    .load(ref)
                    .into(this.sellerProductImg);
        }


        public void setProductID(String productID) {
            this.productID = productID;
        }

    }
}