package com.example.ecommerce;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.ecommerce.R.id.view_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.Model.Products;
import com.example.ViewHolder.ProductViewHolder;
import com.example.utils.SharedPreferenceClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerDashboardActivity extends AppCompatActivity {
    Button add_p,orders_b;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth auth;
    ImageButton log_out_seller;
    ImageView edit_p,delete_p;
    SharedPreferenceClass sharedPreferenceClass;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        add_p = (Button)findViewById(R.id.add_product);
        sharedPreferenceClass =  new SharedPreferenceClass(this);
        getSupportActionBar().hide();
         log_out_seller=(ImageButton)findViewById(R.id.s_log_out);
        orders_b =  (Button) findViewById(view_orders);
         auth = FirebaseAuth.getInstance();
        if(!isSMSPermissionGranted()){
            Toast.makeText(this, "Please Accept SMS permissions", Toast.LENGTH_SHORT).show();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        log_out_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               auth.signOut();
                sharedPreferenceClass.clearData();
                Intent intent = new Intent(SellerDashboardActivity.this, LoginCustomerActivity.class);
                startActivity(intent);
                Toast.makeText(SellerDashboardActivity.this,"Logout successful",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        orders_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerDashboardActivity.this, AdminNewOrdersActivity.class));
            }
        });
        add_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerDashboardActivity.this, AdminCategoryActivity.class));
                //finishActivity();
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart(){
        super.onStart();
        String seller_id = sharedPreferenceClass.getValue_string("seller_id");
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("seller_id").equalTo(seller_id), Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "Rs");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.delete_p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProductsRef.child(model.getPid())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful()){


                                                    Toast.makeText(SellerDashboardActivity.this,"Product Deleted Successfully",Toast.LENGTH_SHORT).show();



                                                }
                                            }
                                        });
                            }
                        });
                         holder.edit_p.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                               final DialogPlus dialogPlus= DialogPlus.newDialog(holder.edit_p.getContext()).setContentHolder(new ViewHolder(R.layout.update_popup)).setExpanded(true,1500).create();
                               dialogPlus.show();
                                 View v=dialogPlus.getHolderView();
                                 EditText Name = v.findViewById(R.id.p_name);

                                 TextView p_description =  v.findViewById(R.id.p_description);
                                 TextView txtProductPrice = (TextView)  v.findViewById(R.id.p_price);
                                 Button button3 =  v.findViewById(R.id.button3);
                                 Name.setText(model.getPname());
                                 p_description.setText(model.getDescription());
                                 txtProductPrice.setText(model.getPrice());
                                 delete_p=(ImageView) v.findViewById(R.id.delete_product);

                                 edit_p=(ImageView) v.findViewById(R.id.update_product);


                                 button3.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         HashMap<String, Object> productMap = new HashMap<>();
                                         productMap.put("description", p_description.getText().toString());
                                         productMap.put("price", txtProductPrice.getText().toString());
                                         productMap.put("pname", Name.getText().toString());
                                         ProductsRef.child(model.getPid())
                                                 .updateChildren(productMap)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task)
                                                     {
                                                         if (task.isSuccessful()){


                                                                 Toast.makeText(SellerDashboardActivity.this,"Product Updated Successfully",Toast.LENGTH_SHORT).show();

                                                                 dialogPlus.dismiss();

                                                         }
                                                     }
                                                 });
                                     }
                                 });

                             }
                         });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public  boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}