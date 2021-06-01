package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    EditText mailAddress, password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mailAddress = (EditText) findViewById(R.id.mailAddress);
        password = (EditText) findViewById(R.id.password);

        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String mail, pass;

        try {
            mail = mailAddress.getText().toString();
            pass = password.getText().toString();
        } catch (Exception e){
            Toast.makeText(this, "The error is " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                //Ana sayfaya götür

                Intent intent = new Intent(LoginScreen.this, MainMenu.class);
                startActivity(intent);
                Log.d("Giriş", "başarı");
                //finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginScreen.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("Giriş", "maalesef");
            }
        });


    }
}