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
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.SharedPreferenceClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginSellerActivity extends AppCompatActivity {
    Button login_seller;
   EditText Email_seller, Password_seller;
   TextView Seller_forgotPassword,SellerLoginPage,seller_signup;
   ImageView logo;
    private FirebaseAuth auth;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_seller2);
        getSupportActionBar().setTitle("Seller Login");
        Email_seller= (EditText) findViewById(R.id.seller_email);
        Password_seller= (EditText) findViewById(R.id.seller_password);
        Seller_forgotPassword= (TextView) findViewById(R.id.seller_forgot_password);
        seller_signup=(TextView) findViewById(R.id.seller_signup);
        login_seller=(Button)findViewById(R.id.login_seller);
        auth = FirebaseAuth.getInstance();
        sharedPreferenceClass =  new SharedPreferenceClass(this);
        seller_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginSellerActivity.this, SellerRegisterActivity.class);
                startActivity(i);
                //finish();

            }
        });
        login_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

       Seller_forgotPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(LoginSellerActivity.this, ForgotPasswordActivity.class);
               startActivity(i);
           }
       });
    }
    public void Login()
    {
        String S_email =Email_seller.getText().toString();
        String S_password = Password_seller.getText().toString();
        if (TextUtils.isEmpty(S_email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(S_password)) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(S_password.length()<6)
        {
            Toast.makeText(this,"Password is too short",Toast.LENGTH_SHORT).show();
            return;
        }
//        auth.signInWithEmailAndPassword(S_email,S_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                if(authResult!=null)
//                {
//                    FirebaseUser user = authResult.getUser();
//                    Toast.makeText(LoginSellerActivity.this, ""+user.getUid(), Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(LoginSellerActivity.this, "login failed", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//
//        });
        auth.signInWithEmailAndPassword(S_email,S_password).addOnCompleteListener(LoginSellerActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    sharedPreferenceClass.setValue_boolean("seller_login",true);
                    sharedPreferenceClass.setValue_string("seller_id",currentUser.getUid());
                //    Toast.makeText(LoginSellerActivity.this, ""+currentUser.getUid(), Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(LoginSellerActivity.this, SellerDashboardActivity.class));
                }
                else
                {
                    Toast.makeText(LoginSellerActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    }
