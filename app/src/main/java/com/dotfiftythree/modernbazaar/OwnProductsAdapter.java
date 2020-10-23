package com.dotfiftythree.modernbazaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OwnProductsAdapter extends RecyclerView.Adapter<OwnProductsAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<OwnProductsList> products = new ArrayList<>();
    private Context context;

    public OwnProductsAdapter(Context context, ArrayList<OwnProductsList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public OwnProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.own_products_item, parent, false);
        return new OwnProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnProductsAdapter.ViewHolder holder, int position) {
        // initialize each element
        final OwnProductsList fetchProductArrayList = products.get(position);
        holder.setProductID(fetchProductArrayList.getProductID());
        holder.setProductName(fetchProductArrayList.getProductName());
        holder.setProductImage(fetchProductArrayList.getProductImage());
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        String productID;
        CardView ownProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ownProductImg);
            productName = itemView.findViewById(R.id.ownProductName);
            ownProduct = itemView.findViewById(R.id.ownProductItem);

            ownProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent transition = new Intent(context, BarterRequestDetail.class);
//                    transition.putExtra(Product.getProductid(), productID);
//                    context.startActivity(transition);
//                    ((Activity) context).finish();
                }
            });


        }


        public void setProductImage(String productImage) {
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(productImage);
            GlideApp.with(context)
                    .load(ref)
                    .into(this.productImage);
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public void setProductName(String productName) {
            this.productName.setText(productName);
        }


    }
}

