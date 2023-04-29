package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminCategoryActivity extends AppCompatActivity {
    private Button fruits, vegetables;

    private Button CheckOrdersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        fruits = (Button) findViewById(R.id.fruitsB);
        vegetables = (Button) findViewById(R.id.vegetablesB);

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                i.putExtra("category", "fruits");
                startActivity(i);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                i.putExtra("category", "vegetables");
                startActivity(i);
            }
        });



    }
}