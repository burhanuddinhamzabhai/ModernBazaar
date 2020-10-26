package com.dotfiftythree.modernbazaar.ProfileFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dotfiftythree.modernbazaar.Constants.Product;
import com.dotfiftythree.modernbazaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class DeactivateAccount extends AppCompatActivity {
    private Button confirm,cancel;
    private LinearLayout back,base;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ProgressDialog progressDialog;
    DatabaseReference userDB = firebaseDatabase.getReference("userDB");
    DatabaseReference product = firebaseDatabase.getReference("ProductsDB");
    ChildEventListener productListener, userListener,storage;
    private HashMap<String, Object> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);
        back=findViewById(R.id.backfromda);
        confirm=findViewById(R.id.confirmdecactivatebtn);
        cancel=findViewById(R.id.canceldecactivatebtn);
        base=findViewById(R.id.base);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(DeactivateAccount.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Deactivating Account...");
                progressDialog.show();
               productListener = new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                       String url="";
                       GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                       };
                       final String _childKey = snapshot.getKey();
                       final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                       if (_childKey.contains(firebaseAuth.getCurrentUser().getUid())) {
                           url = _childValue.get(Product.getImage()).toString();
                           Log.i("name", "onChildAdded: "+url);
                           StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);
                           storageReference.delete();
                           product.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       userListener = new ChildEventListener() {
                                           @Override
                                           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                               final String _childKey = snapshot.getKey();
                                               if (_childKey.equals(firebaseAuth.getCurrentUser().getUid())){
                                                   userDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {
                                                           FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                                           mUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {
                                                                   progressDialog.cancel();
                                                                   Snackbar snackbar = Snackbar.make(base,R.string.deactivationsuccessful, Snackbar.LENGTH_SHORT);
                                                                   snackbar.getView().setBackgroundColor(ContextCompat.getColor(DeactivateAccount.this, R.color.error));
                                                                   snackbar.show();
                                                                   new Handler().postDelayed(new Runnable() {
                                                                       @Override
                                                                       public void run() {

                                                                           finish();
                                                                       }
                                                                   },2000);
                                                               }
                                                           });
                                                       }
                                                   });
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
                                       userDB.addChildEventListener(userListener);
                                   }

                               }
                           });
                       }else{
                           userListener = new ChildEventListener() {
                               @Override
                               public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                   GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                   };
                                   final String _childKey = snapshot.getKey();
                                   final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                                   if (_childKey.equals(firebaseAuth.getCurrentUser().getUid())){
                                       userDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                               mUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       progressDialog.cancel();
                                                       Snackbar snackbar = Snackbar.make(base,R.string.deactivationsuccessful, Snackbar.LENGTH_SHORT);
                                                       snackbar.getView().setBackgroundColor(ContextCompat.getColor(DeactivateAccount.this, R.color.error));
                                                       snackbar.show();
                                                       new Handler().postDelayed(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               finish();
                                                           }
                                                       },2000);
                                                   }
                                               });
                                           }
                                       });
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
                           userDB.addChildEventListener(userListener);
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
                product.addChildEventListener(productListener);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
/*
storage = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        final String _childKey = snapshot.getKey();
                        final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                        if (_childKey.contains(firebaseAuth.getCurrentUser().getUid())) {
                            StorageReference Storage= firebaseStorage.getReferenceFromUrl(_childKey);
                            Storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    productListener = new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                            };
                                            final String _childKey = snapshot.getKey();
                                            final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                                            if (_childKey.contains(firebaseAuth.getCurrentUser().getUid())) {
                                                product.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        userDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                user.delete()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    progressDialog.cancel();
                                                                                    Snackbar snackbar = Snackbar.make(base,R.string.deactivationsuccessful, Snackbar.LENGTH_SHORT);
                                                                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(DeactivateAccount.this, R.color.error));
                                                                                    snackbar.show();
                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            Intent transition = new Intent(DeactivateAccount.this, MainActivity.class);
                                                                                            startActivity(transition);
                                                                                            finish();
                                                                                        }
                                                                                    },2000);


                                                                                }
                                                                            }
                                                                        });


                                                            }
                                                        });
                                                    }
                                                });
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

                                }
                            });
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
 */