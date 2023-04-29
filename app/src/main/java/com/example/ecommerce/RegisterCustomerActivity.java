package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterCustomerActivity extends AppCompatActivity {

    private Button login;
    private EditText input_email, input_password, input_customer_name;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_customer);
        getSupportActionBar().setTitle("User Registration");
        input_email = (EditText) findViewById(R.id.customer_email);
        input_password = (EditText) findViewById(R.id.customer_input_password);
        input_customer_name = (EditText) findViewById(R.id.customer_name);
        auth = FirebaseAuth.getInstance();

    }
    public void signup(View view)
    {
        String name = input_customer_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your Phone number", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length()<6)
        {
            Toast.makeText(this,"Password is too short, enter minimum 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterCustomerActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterCustomerActivity.this,"successfully Registered",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterCustomerActivity.this, CustomerDashboardActivity.class));
                }
                else
                {
                    Toast.makeText(RegisterCustomerActivity.this,"Unsuccessful Registration"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}