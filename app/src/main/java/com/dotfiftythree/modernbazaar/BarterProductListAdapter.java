package com.dotfiftythree.modernbazaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BarterProductListAdapter extends RecyclerView.Adapter<BarterProductListAdapter.ViewHolder> {
    RecyclerView recyclerView;
    ArrayList<BarterProductList> products = new ArrayList<>();
    private Context context;
    String phone;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reqBarter = firebaseDatabase.getReference("BarterDB");
    DatabaseReference userDetails = firebaseDatabase.getReference("userDB");
    ChildEventListener userDetailsFetcher;
    private ProgressDialog progressDialog;
    private HashMap<String, Object> map = new HashMap<>();
    private String barterString = "";

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
        holder.setProductID(fetchProductArrayList.getProductID());
        holder.setBarterProductID(fetchProductArrayList.getBarterProductID());
        holder.setSellerID(fetchProductArrayList.getSellerID());
        holder.setBuyerProductImage(fetchProductArrayList.getBuyerProductImg());
        holder.setSellerProductimage(fetchProductArrayList.getSellerProductImg());


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;

        String productID, barterProductID, sellerID, buyerProductImg, sellerProductImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.barterproductname);

            userDetailsFetcher = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String mphone;
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    final String _childKey = snapshot.getKey();
                    final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                    if (_childKey.equals(mAuth.getCurrentUser().getUid())) {
                        mphone = _childValue.get(User.getPhone()).toString();
                        Log.i("TAG1", "onChildAdded: " + phone);
                        phone = mphone;
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
            userDetails.addChildEventListener(userDetailsFetcher);


            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder request = new AlertDialog.Builder(context);
                    request.setTitle("Send Request with")
                            .setPositiveButton("Call only", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendReqWithPhone();
                                }
                            })
                            .setNegativeButton("Both", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendReqWithBoth();
                                }
                            })
                            .setNeutralButton("None", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendReq();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
        }

        private void sendReq() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Sending Barter Request...");
            progressDialog.setProgress(0);
            progressDialog.show();
            final Date today = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            final String dateToStr = format.format(today);
            map = new HashMap<>();
            barterString = reqBarter.push().getKey();
            map.put(Barter.getBarterID(), barterString);
            map.put(Barter.getDate(), dateToStr);
            map.put(Barter.getBuyerProduct(), productID);//own product of user
            map.put(Barter.getSellerProduct(), barterProductID);//product of seller
            map.put(Barter.getBuyer(), FirebaseAuth.getInstance().getCurrentUser().getUid());//buyer
            map.put(Barter.getSeller(), sellerID);
            map.put(Barter.getBuyerProductImg(), buyerProductImg);
            map.put(Barter.getSellerProductImg(), sellerProductImg);
            reqBarter.child(barterString).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.cancel();
                    map.clear();
                    Toast.makeText(context, R.string.reqsent, Toast.LENGTH_SHORT).show();
                    Intent transition = new Intent(context, HomeViewInDetail.class);
                    transition.putExtra(Product.getProductid(), productID);
                    context.startActivity(transition);
                    ((Activity) context).finish();
                }
            });
        }


        private void sendReqWithBoth() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Sending Barter Request...");
            progressDialog.setProgress(0);
            progressDialog.show();


            final Date today = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            final String dateToStr = format.format(today);
            map = new HashMap<>();
            barterString = reqBarter.push().getKey();
            map.put(Barter.getBarterID(), barterString);
            map.put(Barter.getDate(), dateToStr);
            map.put(Barter.getBuyerProduct(), productID);//own product of user
            map.put(Barter.getSellerProduct(), barterProductID);//product of seller
            map.put(Barter.getBuyer(), FirebaseAuth.getInstance().getCurrentUser().getUid());//buyer
            map.put(Barter.getSeller(), sellerID);
            map.put(Barter.getBuyerMobile(), phone);
            map.put(Barter.getBuyerProductImg(), buyerProductImg);
            map.put(Barter.getSellerProductImg(), sellerProductImg);
            Log.i("TAG2", "onChildAdded: " + phone);
            map.put(Barter.getBuyerMail(), FirebaseAuth.getInstance().getCurrentUser().getEmail());

            reqBarter.child(barterString).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    map.clear();
                    progressDialog.cancel();
                    Toast.makeText(context, R.string.reqsent, Toast.LENGTH_SHORT).show();
                    Intent transition = new Intent(context, HomeViewInDetail.class);
                    transition.putExtra(Product.getProductid(), productID);
                    context.startActivity(transition);
                    ((Activity) context).finish();
                }
            });
        }

        private void sendReqWithPhone() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Sending Barter Request...");
            progressDialog.setProgress(0);
            progressDialog.show();

            final Date today = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            final String dateToStr = format.format(today);
            map = new HashMap<>();
            barterString = reqBarter.push().getKey();
            map.put(Barter.getBarterID(), barterString);
            map.put(Barter.getDate(), dateToStr);
            map.put(Barter.getBuyerProduct(), productID);//own product of user
            map.put(Barter.getSellerProduct(), barterProductID);//product of seller
            map.put(Barter.getBuyer(), FirebaseAuth.getInstance().getCurrentUser().getUid());//buyer
            map.put(Barter.getSeller(), sellerID);
            map.put(Barter.getBuyerMobile(), phone);
            map.put(Barter.getBuyerProductImg(), buyerProductImg);
            map.put(Barter.getSellerProductImg(), sellerProductImg);

            reqBarter.child(barterString).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.cancel();
                    map.clear();
                    Toast.makeText(context, R.string.reqsent, Toast.LENGTH_SHORT).show();
                    Intent transition = new Intent(context, HomeViewInDetail.class);
                    transition.putExtra(Product.getProductid(), productID);
                    context.startActivity(transition);
                    ((Activity) context).finish();
                }
            });
        }


        public void setProductName(String productName) {
            this.productName.setText(productName);
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public void setBarterProductID(String barterProductID) {
            this.barterProductID = barterProductID;

        }

        public void setSellerID(String sellerID) {
            this.sellerID = sellerID;
        }

        public void setBuyerProductImage(String buyerProductImg) {
            this.buyerProductImg = buyerProductImg;
        }

        public void setSellerProductimage(String sellerProductImg) {
            this.sellerProductImg = sellerProductImg;
        }
    }
}