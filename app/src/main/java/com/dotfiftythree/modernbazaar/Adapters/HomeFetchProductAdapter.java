package com.dotfiftythree.modernbazaar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotfiftythree.modernbazaar.ArrayLists.FetchProductArrayList;
import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.HomeFragment.HomeViewInDetail;
import com.dotfiftythree.modernbazaar.LoadingImage.GlideApp;
import com.dotfiftythree.modernbazaar.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFetchProductAdapter extends RecyclerView.Adapter<HomeFetchProductAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<FetchProductArrayList> products = new ArrayList<>();
    private Context context;

    public HomeFetchProductAdapter(Context context, ArrayList<FetchProductArrayList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public HomeFetchProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFetchProductAdapter.ViewHolder holder, int position) {
        // initialize each element
        final FetchProductArrayList fetchProductArrayList = products.get(position);
        holder.setProductImage(fetchProductArrayList.getProductImage());
        holder.setProductName(fetchProductArrayList.getProductName());
        holder.setProductDes(fetchProductArrayList.getProductDes());
        holder.setPeriod(fetchProductArrayList.getPeriodOfUsage());
        holder.setUrl(fetchProductArrayList.getProductID());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDes, periodOfUsage;
        Button detailViewBtn;
        String mUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.homeproductphoto);
            productName = itemView.findViewById(R.id.homeproductname);
            productDes = itemView.findViewById(R.id.homeproductdes);
            periodOfUsage = itemView.findViewById(R.id.homeproductperiod);
            detailViewBtn = itemView.findViewById(R.id.homeproductindetail);
            detailViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transition = new Intent(context, HomeViewInDetail.class);
                    transition.putExtra(Product.getProductid(), mUrl);
                    context.startActivity(transition);
                }
            });
        }

        public void setProductImage(String productImage) {
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(productImage);
            GlideApp.with(context)
                    .load(ref)
                    .into(this.productImage);
        }

        public void setProductName(String productName) {
            this.productName.setText(productName);
        }

        public void setProductDes(String productDes) {
            this.productDes.setText(productDes);
        }

        public void setPeriod(String periodOfUsage) {
            this.periodOfUsage.setText(periodOfUsage);
        }

        public void setUrl(String productID) {
            mUrl = productID;
        }
    }
}
