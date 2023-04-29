package com.example.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class orderAdapter extends ArrayAdapter<String> {

    Activity activity;

    ArrayList<String> qt ;
    ArrayList<String> contact ;
    ArrayList<String> UAddress ;

    public orderAdapter(Activity activity,ArrayList<String> qt, ArrayList<String> contact, ArrayList<String> UAddress ) {
        super(activity, R.layout.orders_layout, contact);
        this.activity = activity;
        this.qt = qt;
        this.contact = contact;
        this.UAddress = UAddress;

        Log.d("CHECK", "orderAdapter: called");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View root = inflater.inflate(R.layout.orders_layout, null, true);

        TextView totalAmount = (TextView) root.findViewById(R.id.order_total_price);
        TextView phone =(TextView)  root.findViewById(R.id.order_phone_number);
        TextView address = (TextView) root.findViewById(R.id.order_address_city);
        Button btn = root.findViewById(R.id.show_all_product_btn);

        try{
            totalAmount.setText(qt.get(position));
            phone.setText(contact.get(position));
            address.setText(UAddress.get(position));
        }
        catch(Exception e){
            Toast.makeText(activity, "error in order fetching", Toast.LENGTH_SHORT).show();
        }

        return  root;
    }
}
