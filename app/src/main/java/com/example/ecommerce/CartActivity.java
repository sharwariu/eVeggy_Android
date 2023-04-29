package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Cart;
import com.example.Model.Products;
import com.example.Prevalent.Prevalent;
import com.example.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessButton;
    private TextView txtTotalAmount,txtMsg1;

     int overTotalPrice = 0;
    Map<String, String> data;
    JSONObject jsonData;
    JSONArray obj = new JSONArray();
    ArrayList<Cart> interestList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("My Cart");
        recyclerView =findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessButton=(Button) findViewById(R.id.next_process_btn);
        txtTotalAmount=(TextView) findViewById(R.id.t_price);
        txtMsg1=(TextView)findViewById(R.id.msg1);

        if(overTotalPrice==0)
        {
            txtTotalAmount.setText("No Products in cart");

        }





    }

    @Override
    protected void onStart()
    {
        super.onStart();

     //   CheckOrderState();
      data =  new HashMap<>();
      interestList = new ArrayList<>();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("Products"),Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Quantity: "+ model.getQuantity());
                holder.txtProductName.setText("Name: " + model.getPname() );
                holder.txtProductPrice.setText("Price: " + model.getPrice());
                String number  = model.getPrice().replaceAll("[^0-9]", "");
                String q  = model.getQuantity().replaceAll("[^0-9]", "");




                        // 1st object

                try {
                    JSONObject cust= new JSONObject();
                    cust.put("p_id",model.getPid());
                    cust.put("name",model.getPname());
                    cust.put("price",number);
                    cust.put("qty",q);
                    cust.put("seller_id",model.getSeller_id());
                    obj.put(cust);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                ;
              //  int oneTypeProductTPrice = ((Integer.parseInt(number))) * ((Integer.parseInt(model.getQuantity())));
                //Toast.makeText(CartActivity.this, ""+number, Toast.LENGTH_SHORT).show();
                int s = Integer.parseInt(number) * Integer.parseInt(q);
                overTotalPrice = overTotalPrice + s;
                txtTotalAmount.setText("Total Price ₹"+overTotalPrice);

                if(overTotalPrice>0)
                {
                    NextProcessButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            txtTotalAmount.setText(String.valueOf(overTotalPrice));
                            Log.d("sds",""+obj);

                            Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                            intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                            intent.putExtra("order_product",obj.toString());
                            startActivity(intent);


                        }
                    });
                }
                else
                {
                    NextProcessButton.setText("Empty");
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i == 1)
                                {
                                    cartListRef.child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this,"Item removed Successfully.",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this,CustomerDashboardActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });


                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public JSONObject makJsonObject(int id[], String name[], String price[],
                                    String qty[], String seller_id[], int carts)
            throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < carts; i++) {
            obj = new JSONObject();
            try {
                obj.put("product_id", id[i]);
                obj.put("name", name[i]);
                obj.put("price", price[i]);
                obj.put("qty", qty[i]);
                obj.put("seller_id", seller_id[i]);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        JSONObject finalobject = new JSONObject();
        finalobject.put("student", jsonArray);
        return finalobject;
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    if (shippingState.equals("Shipped")){
                        txtTotalAmount.setText("TDear order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your Final order has been shipped successfully. Soon you will received your order at your door step.");

                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products, Once you received your first order",Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("Not Shipped")){
                        txtTotalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);

                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products, Once you received your first order",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}