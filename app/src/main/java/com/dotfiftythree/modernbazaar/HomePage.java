package com.dotfiftythree.modernbazaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomePage extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth mAuth;
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomNav=findViewById(R.id.bottomNav);
//        logout = findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.home,true);
            fragmentManager = getSupportFragmentManager();
            home homeFragment = new home();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer,homeFragment)
                    .commit();

        }
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id){
                    case R.id.home:
                        fragment = new home();
                        break;

                    case R.id.products:
                        fragment = new products();
                        break;

                    case R.id.profile:
                        fragment = new profile();
                        break;


                }
                if(fragment!=null){
                    fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer,fragment)
                            .commit();
                }else {
                    Log.e("FRAGMENTERROR","Error in creating fragment");
                }
            }
        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Intent transition = new Intent(HomePage.this,MainActivity.class);
//                startActivity(transition);
//                finish();
//            }
//        });
    }

}