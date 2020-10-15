package com.dotfiftythree.modernbazaar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class profile extends Fragment {


    private LinearLayout insert,logout,deactivate,base;
    FirebaseAuth mAuth;
    public profile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        insert= v.<LinearLayout>findViewById(R.id.insertproductlin);
        logout= v.<LinearLayout>findViewById(R.id.logoutlin);
        deactivate=v.<LinearLayout>findViewById(R.id.deactivatelin);
        base=v.<LinearLayout>findViewById(R.id.base);

        mAuth=FirebaseAuth.getInstance();
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(getActivity(), InsertProduct.class);
                startActivity(transition);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent transition = new Intent(getActivity(), MainActivity.class);
                startActivity(transition);
                getActivity().finish();
            }
        });
        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}