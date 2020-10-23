package com.dotfiftythree.modernbazaar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;


public class Profile extends Fragment {


    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    DatabaseReference userDetails = firebaseDatabase.getReference("userDB");
    ChildEventListener userDetailsListener;
    String userName, userGender;
    private LinearLayout insert, logout, deactivate, base, newReq, recentBarterLin, savedItemLin, complain;
    private ImageView userImage;
    private TextView profileName;

    public Profile() {
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
        insert = v.findViewById(R.id.insertproductlin);
        logout = v.findViewById(R.id.logoutlin);
        deactivate = v.findViewById(R.id.deactivatelin);
        base = v.findViewById(R.id.base);
        userImage = v.findViewById(R.id.userImage);
        profileName = v.findViewById(R.id.profilename);
        newReq = v.findViewById(R.id.newrequest);
        recentBarterLin = v.findViewById(R.id.recentbarterlin);
        savedItemLin = v.findViewById(R.id.saveditemlin);
        complain = v.findViewById(R.id.filecomplainlin);

        mAuth = FirebaseAuth.getInstance();
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
                Intent transition = new Intent(getActivity(), DeactivateAccount.class);
                startActivity(transition);
            }
        });
        newReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(getActivity(), NewBarterRequest.class);
                startActivity(transition);
            }
        });
        recentBarterLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(getActivity(), RecentBarterRequests.class);
                startActivity(transition);
            }
        });
        savedItemLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(getActivity(), SavedItems.class);
                startActivity(transition);
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transition = new Intent(getActivity(), FileAComplain.class);
                startActivity(transition);
            }
        });


        userDetailsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name, gender;
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(mAuth.getCurrentUser().getUid())) {
                    name = _childValue.get(User.getName()).toString();
                    gender = _childValue.get(User.getGender()).toString();
                    userName = name;
                    userGender = gender;
                    if (userGender.equals("male")) {
                        userImage.setImageResource(R.drawable.male);
                    }
                    profileName.setText("Hello\n" + userName);

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
        userDetails.addChildEventListener(userDetailsListener);


        // Inflate the layout for this fragment
        return v;
    }
}