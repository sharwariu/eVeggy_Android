package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.SharedPreferenceClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginCustomerActivity extends AppCompatActivity {
     Button signup_customer,login_customer;
     EditText customerEmail,customerPassword;
     FirebaseAuth auth;
     SharedPreferenceClass sharedPreferenceClass;
     TextView seller_login,forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);
        getSupportActionBar().setTitle("User Login");
        sharedPreferenceClass =  new SharedPreferenceClass(this);
        customerEmail= (EditText) findViewById(R.id.customer_email);
        customerPassword= (EditText) findViewById(R.id.customer_input_password);
        signup_customer=(Button)findViewById(R.id.signup_customer);
        login_customer=(Button)findViewById(R.id.login_customer);
        seller_login=(TextView)findViewById(R.id.seller_login);
        forgot_password=(TextView)findViewById(R.id.user_forgot_password);
        auth = FirebaseAuth.getInstance();
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginCustomerActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        try {
            signup_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginCustomerActivity.this, RegisterCustomerActivity.class);
                    startActivity(i);
                    //finish();

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "sign customer"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{

        seller_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginCustomerActivity.this,LoginSellerActivity.class);
                startActivity(i);
            }
        });
        }catch (Exception e){
            Toast.makeText(this, "seller login"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
        login_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login_customer();
            }
        });
        }catch (Exception e){
            Toast.makeText(this, "slogin customer"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void Login_customer()
    {
        String C_email =customerEmail.getText().toString();
        String C_password = customerPassword.getText().toString();
         if (TextUtils.isEmpty(C_email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(C_password)) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(C_password.length()<6)
        {
            Toast.makeText(this,"Password is too short",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(C_email,C_password).addOnCompleteListener(LoginCustomerActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    sharedPreferenceClass.setValue_boolean("user_login",true);
                    sharedPreferenceClass.setValue_string("user_id",currentUser.getUid());
                    startActivity(new Intent(LoginCustomerActivity.this, CustomerDashboardActivity.class));
                }
                else
                {
                    Toast.makeText(LoginCustomerActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}