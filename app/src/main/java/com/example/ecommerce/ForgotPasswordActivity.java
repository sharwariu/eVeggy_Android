package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button resetlink;
    EditText email;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Forget Password");
        setContentView(R.layout.activity_forgot_password2);
        resetlink=(Button) findViewById(R.id.reset_link);
        email=(EditText) findViewById(R.id.email_reset_pass);
        auth= FirebaseAuth.getInstance();
        resetlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(ForgotPasswordActivity.this, "Password link is send", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(ForgotPasswordActivity.this, "Failed to send, Try again"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });
    }
}