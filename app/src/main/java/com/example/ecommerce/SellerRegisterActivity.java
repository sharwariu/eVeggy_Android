package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerRegisterActivity extends AppCompatActivity {
  Button SellerLogin;
  EditText sellEmail,sellName,sellPassword;
  ImageView imageView;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        getSupportActionBar().setTitle("Seller Registration");
        sellEmail = (EditText) findViewById(R.id.Sell_email);
        sellPassword= (EditText) findViewById(R.id.Sell_password);
        sellName = (EditText) findViewById(R.id.Sell_name);
        //getSupportActionBar().hide();
        SellerLogin=(Button)findViewById(R.id.SellLogin);
        auth = FirebaseAuth.getInstance();
SellerLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        signup(view);
    }
});
    }
    public void signup(View view)
    {
        String Name_se = sellName.getText().toString();
        String email_se = sellEmail.getText().toString();
        String password_se = sellPassword.getText().toString();
        if (TextUtils.isEmpty(Name_se)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email_se)) {
            Toast.makeText(this, "Please enter your Phone number", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password_se)) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password_se.length()<6)
        {
            Toast.makeText(this,"Password is too short, enter minimum 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email_se,password_se).addOnCompleteListener(SellerRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SellerRegisterActivity.this,"successfully Registered",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SellerRegisterActivity.this, SellerDashboardActivity.class));
                }
                else
                {
                    Toast.makeText(SellerRegisterActivity.this,"Unsuccessful Registration"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
