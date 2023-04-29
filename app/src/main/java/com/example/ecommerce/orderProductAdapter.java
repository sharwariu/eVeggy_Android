package com.example.ecommerce;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Model.Cart;

import java.util.ArrayList;

public class orderProductAdapter extends BaseAdapter {

    Activity activity;

   ArrayList<Cart> cartArrayList;

    public orderProductAdapter(Activity activity, ArrayList<Cart> cartArrayList) {
        this.activity = activity;
       this.cartArrayList = cartArrayList;

        Log.d("CHECK", "orderAdapter: called");
    }

    @Override
    public int getCount() {
        return this.cartArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cart model =  cartArrayList.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View root = inflater.inflate(R.layout.order_detail_item, null, true);

        TextView product_price = (TextView) root.findViewById(R.id.product_price);
        TextView name =(TextView)  root.findViewById(R.id.name);
        TextView qty = (TextView) root.findViewById(R.id.qty);
        product_price.setText(""+model.getPrice());
        name.setText(""+model.getPname());
        qty.setText(""+model.getQuantity());



        return  root;
    }
}
