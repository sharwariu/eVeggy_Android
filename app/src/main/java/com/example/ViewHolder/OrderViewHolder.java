package com.example.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView order_id,name,date,total,state,accept,reject,address,order_details;
    public Spinner action;
    public ImageView imageView,edit_p,delete_p;
    LinearLayout action_button;
    public ItemClickListner listner;

    public OrderViewHolder(View itemView)
    {
        super(itemView);
        action = (Spinner) itemView.findViewById(R.id.action);
        action_button = (LinearLayout) itemView.findViewById(R.id.action_button);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        order_id = (TextView) itemView.findViewById(R.id.order_id);
        address = (TextView) itemView.findViewById(R.id.address);
        name = (TextView) itemView.findViewById(R.id.name);
        accept = (TextView) itemView.findViewById(R.id.accept);
        reject = (TextView) itemView.findViewById(R.id.reject);
        date = (TextView) itemView.findViewById(R.id.date);
        total = (TextView) itemView.findViewById(R.id.total);
        state = (TextView) itemView.findViewById(R.id.state);
        order_details  = (TextView) itemView.findViewById(R.id.order_details);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }


}
