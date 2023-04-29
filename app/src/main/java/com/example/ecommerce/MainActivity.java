package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.utils.SharedPreferenceClass;

public class MainActivity extends AppCompatActivity {
    public Button Next;
    SharedPreferenceClass sharedPreferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sharedPreferenceClass =  new SharedPreferenceClass(this);

        Next= (Button) findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(sharedPreferenceClass.getValue_boolean("seller_login"))
                    {
                        Intent intent = new Intent(MainActivity.this, SellerDashboardActivity.class);
                        startActivity(intent);
                        return;
                    }

                    if(sharedPreferenceClass.getValue_boolean("user_login"))
                    {
                        Intent intent = new Intent(MainActivity.this, CustomerDashboardActivity.class);
                        startActivity(intent);
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, LoginCustomerActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}