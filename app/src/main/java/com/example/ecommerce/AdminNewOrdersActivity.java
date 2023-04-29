package com.example.ecommerce;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.AdminOrders;
import com.example.Model.Cart;
import com.example.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {


    private RecyclerView order_list;
    FirebaseAuth auth;
    private DatabaseReference ordersRef;

    private RecyclerView ordersList;
    private DatabaseReference qRef,phoneRef, addressRef  ;
    FirebaseDatabase firebaseDatabase;
 ArrayList<Cart> OrderDetailList;
    ListView orderList;
  String  SelectedStatus = "";
    ArrayList<String> qt ;
    ArrayList<String> phone ;
    ArrayList<String> address ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        getSupportActionBar().setTitle("New Orders");
        order_list =  findViewById(R.id.order_list);
        auth = FirebaseAuth.getInstance();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        order_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        order_list.setLayoutManager(layoutManager);


//        firebaseDatabase = FirebaseDatabase.getInstance();
//        qRef = firebaseDatabase.getReference("/Orders/totalAmount");
//        phoneRef = firebaseDatabase.getReference("/Orders/phone");
//        addressRef = firebaseDatabase.getReference("/Orders/address");
//
//        orderList = findViewById(R.id.order_list);
//
//        qt = new ArrayList<>();
//        phone = new ArrayList<>();
//        address = new ArrayList<>();
//
//        new fetchOrders().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                        holder.total.setText("Total Order Price -  ₹"+model.getTotalAmount());
                        holder.date.setText("Order Date - "+model.getDate());
                        holder.state.setText("Your Order is "+model.getState());
                        if(model.getOrder_products()!=null) {
                            holder.order_details.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    OrderDetailList = new ArrayList<>();
                                    final DialogPlus dialogPlus = DialogPlus.newDialog(AdminNewOrdersActivity.this).setContentHolder(new ViewHolder(R.layout.order_details)).setExpanded(true, 800).create();
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
                                            orderProductAdapter orderProductAdapter = new orderProductAdapter(AdminNewOrdersActivity.this, OrderDetailList);
                                            list.setAdapter(orderProductAdapter);
                                        }


                                        //Toast.makeText(UserOrdersActivity.this, ""+array, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }
                        String[] actions = { "Accepted","Processing","Processed",
                                "Shipped", "InTransit",
                                "Rejected", "Hold" };
                        ArrayAdapter ad
                                = new ArrayAdapter(
                                AdminNewOrdersActivity.this,
                                android.R.layout.simple_spinner_item,
                                actions);

                        // set simple layout resource file
                        // for each item of spinner
                        ad.setDropDownViewResource(
                                android.R.layout
                                        .simple_spinner_dropdown_item);

                        // Set the ArrayAdapter (ad) data on the
                        // Spinner which binds data to spinner
                        holder.action.setAdapter(ad);
                        for (int i=0;i<actions.length;i++)
                        {
                            if(actions[i].equals(model.getState()))
                            {
                                holder.action.setSelection(i);
                            }
                        }

                        holder.action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                SelectedStatus = actions[i];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        holder.accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                HashMap<String, Object> ordersMap = new HashMap<>();
                                ordersMap.put("state", SelectedStatus);
                                ordersRef.child(model.getOrder_id())
                                        .updateChildren(ordersMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful()){
                                                    if(isSMSPermissionGranted()){
                                                        sendSms(model.getPhone(),"Hi "+model.getName()+" your order Id - "+model.getOrder_id()+" of "+model.getTotalAmount()+" is "+SelectedStatus);
                                                        Toast.makeText(AdminNewOrdersActivity.this,"Order "+SelectedStatus+" Successfully.",Toast.LENGTH_SHORT).show();
                                                    }



                                                }
                                            }
                                        });
                            }
                        });
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

    //        class fetchOrders extends AsyncTask<Void, Void, Void>{
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                qRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String quantity = snapshot.getValue(String.class);
//                        qt.add(quantity);
//
//                        phoneRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String phoneNo = snapshot.getValue(String.class);
//                                Log.d("CHECK", "onDataChange: "+phoneNo);
//                                Toast.makeText(AdminNewOrdersActivity.this, phoneNo, Toast.LENGTH_SHORT).show();
//                                phone.add(phoneNo);
//
//                                addressRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        String UserAddress = snapshot.getValue(String.class);
//                                        address.add(UserAddress);
//
//                                        orderAdapter orderAdapter = new orderAdapter(AdminNewOrdersActivity.this, qt, phone, address);
//                                        orderList.setAdapter(orderAdapter);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
//
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void unused) {
//
//
//
//            }
//        }

    private void sendSms(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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