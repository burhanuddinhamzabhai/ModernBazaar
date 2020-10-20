package com.dotfiftythree.modernbazaar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BarterProductListAdapter extends RecyclerView.Adapter<BarterProductListAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<BarterProductList> products = new ArrayList<>();
    private Context context;

    public BarterProductListAdapter(Context context, ArrayList<BarterProductList> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public BarterProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for recycleview item
        View view = LayoutInflater.from(context).inflate(R.layout.barter_list_item, parent, false);
        return new BarterProductListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarterProductListAdapter.ViewHolder holder, int position) {
        // initialize each element
        final BarterProductList fetchProductArrayList = products.get(position);
        holder.setProductName(fetchProductArrayList.getProductName());
        holder.setUrl(fetchProductArrayList.getProductID());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;

        String mUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.barterproductname);
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transition = new Intent(context, VisitorViewProductDetail.class);
                    transition.putExtra(Product.getProductid(), mUrl);
                    context.startActivity(transition);
                }
            });
        }


        public void setProductName(String productName) {
            this.productName.setText(productName);
        }

        public void setUrl(String productID) {
            mUrl = productID;
        }
    }
}