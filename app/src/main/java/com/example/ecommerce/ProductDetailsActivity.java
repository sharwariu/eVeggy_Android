package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    Button incBtn,decBtn;
    TextView quanView;
    private ImageView productImage;
    private TextView pName,pDescription,pPrice;
    private String productID="";
    private String Seller_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


            productID=getIntent().getStringExtra("pid");
            //BuyNow = (Button)findViewById(R.id.buy_now);
            addToCartButton=(Button)findViewById(R.id.pd_add_to_cart_button);
            productImage=( ImageView)findViewById(R.id.product_image_details);
            pName=(TextView)findViewById(R.id.product_name_details);
            pDescription=(TextView)findViewById(R.id.product_description_details);
            pPrice=(TextView)findViewById(R.id.product_price_details);

            getProductDeatils(productID);
//            BuyNow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(ProductDetailsActivity.this,ConfirmFinalOrderActivity.class);
//                    startActivity(intent);
//                }
//            });
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    addingToCartList();
                }
            });

            incBtn=(Button)findViewById(R.id.plusbtn);
            decBtn=(Button) findViewById(R.id.minbtn);
            quanView=(TextView) findViewById(R.id.quantity_view);

            incBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentValue=quanView.getText().toString();
                    int value = Integer.parseInt(currentValue);
                    value++;
                    quanView.setText(String.valueOf(value));

                }
            });

            decBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentValue=quanView.getText().toString();
                    int value = Integer.parseInt(currentValue);
                    value--;
                    quanView.setText(String.valueOf(value));




                }
            });




        }

        private void addingToCartList()
        {
            String saveCurrentTime, saveCurrentDate;

            Calendar calforDate= Calendar.getInstance();
            SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate=currentDate.format(calforDate.getTime());

            SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime=currentDate.format(calforDate.getTime());

            final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

            final HashMap<String,Object> cartMap=new HashMap<>();
            cartMap.put("pid",productID);
            cartMap.put("seller_id",Seller_id);
            cartMap.put("pname",pName.getText().toString());
            cartMap.put("price",pPrice.getText().toString()+ "₹");
            cartMap.put("date",saveCurrentDate);
            cartMap.put("time",saveCurrentTime);
            cartMap.put("quantity",quanView.getText().toString() + "KG");

            cartListRef.child("Products").child(productID)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                cartListRef.child("Products").child(productID)
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                                                    Intent intent=new Intent(ProductDetailsActivity.this,CartActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                            }
                        }
                    });






        }

        private void getProductDeatils(String productID) {

            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

            productRef.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        Products products = snapshot.getValue(Products.class);
                        //Toast.makeText(ProductDetailsActivity.this, ""+products.getSeller_id(), Toast.LENGTH_SHORT).show();
                        Seller_id = products.getSeller_id();
                        pName.setText(products.getPname());
                        pPrice.setText(products.getPrice());
                        pDescription.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productImage);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });

        }
}