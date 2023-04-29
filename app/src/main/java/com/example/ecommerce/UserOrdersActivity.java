package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Model.AdminOrders;
import com.example.Model.Cart;
import com.example.Model.Products;
import com.example.ViewHolder.OrderViewHolder;
import com.example.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserOrdersActivity extends AppCompatActivity {
    private RecyclerView order_list;
    FirebaseAuth auth;
    private DatabaseReference ordersRef;
    ArrayList<Cart> OrderDetailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);
        getSupportActionBar().setTitle("My Orders");
        order_list =  findViewById(R.id.order_list);
        auth = FirebaseAuth.getInstance();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        order_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        order_list.setLayoutManager(layoutManager);
        CheckOrderState();
    }

    @Override
    protected void onStart() {
        super.onStart();
    Log.d("dfdfd",ordersRef.toString());

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull AdminOrders model)
                    {

                        holder.order_id.setText("#Order Id - "+model.getOrder_id());
                        holder.name.setText(model.getName());
                        holder.address.setText("Address - "+model.getAddress()+" "+model.getCity());
                        holder.reject.setVisibility(View.GONE);
                        holder.accept.setVisibility(View.GONE);
                        holder.action.setVisibility(View.GONE);
                        holder.total.setText("Total Order Price -  ₹"+model.getTotalAmount());
                        holder.date.setText("Order Date - "+model.getDate());
                        holder.state.setText("Your Order is "+model.getState());
                        if(model.getOrder_products()!=null) {
                            holder.order_details.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    OrderDetailList = new ArrayList<>();
                                    final DialogPlus dialogPlus = DialogPlus.newDialog(UserOrdersActivity.this).setContentHolder(new ViewHolder(R.layout.order_details)).setExpanded(true, 800).create();
                                    dialogPlus.show();
                                    View v = dialogPlus.getHolderView();
                                    ListView list = v.findViewById(R.id.orderList);
                                    try {

                                        JSONArray array = new JSONArray(model.getOrder_products());
                                        if (array.length() > 0) {
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject obj = array.getJSONObject(i);
                                                Cart products = new Cart();
                                                products.setPname("Product Name - " + String.valueOf(obj.get("name")));
                                                products.setPid(String.valueOf(obj.get("p_id")));
                                                products.setPrice("Product Price - " + String.valueOf(obj.get("price")));
                                                products.setQuantity("Product Quantity - " + String.valueOf(obj.get("qty")));
//                                            products.setSeller_id(String.valueOf(obj.get("seller_id")));
                                                //Toast.makeText(UserOrdersActivity.this, ""+obj.get("name"), Toast.LENGTH_SHORT).show();
                                                OrderDetailList.add(products);

                                            }
                                         //   Toast.makeText(UserOrdersActivity.this, "" + OrderDetailList.size(), Toast.LENGTH_SHORT).show();
                                            orderProductAdapter orderProductAdapter = new orderProductAdapter(UserOrdersActivity.this, OrderDetailList);
                                            list.setAdapter(orderProductAdapter);
                                        }


                                        //Toast.makeText(UserOrdersActivity.this, ""+array, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }
                        //holder.txtProductDescription.setText(model.getAddress());
                        //holder.txtProductPrice.setText("Price = " + model.getTotalAmount() + "Rs");
                       // Toast.makeText(UserOrdersActivity.this, ""+model.getName(), Toast.LENGTH_SHORT).show();

                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }
                };
        order_list.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState()
    {



//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    Log.d("chetan",dataSnapshot.toString());
//                    String shippingState = dataSnapshot.child("state").getValue().toString();
//                    Toast.makeText(UserOrdersActivity.this, ""+shippingState, Toast.LENGTH_SHORT).show();
//                    String userName = dataSnapshot.child("name").getValue().toString();
//                    if (shippingState.equals("Shipped")){
////                        txtTotalAmount.setText("TDear order is shipped successfully.");
////                        recyclerView.setVisibility(View.GONE);
////                        txtMsg1.setVisibility(View.VISIBLE);
////                        txtMsg1.setText("Congratulations, Your Final order has been shipped successfully. Soon you will received your order at your door step.");
////
////                        NextProcessButton.setVisibility(View.GONE);
//
//                    }
//                    else if (shippingState.equals("Not Shipped")){
////                        txtTotalAmount.setText("Shipping State = Not Shipped");
////                        recyclerView.setVisibility(View.GONE);
////                        txtMsg1.setVisibility(View.VISIBLE);
////
////                        NextProcessButton.setVisibility(View.GONE);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}