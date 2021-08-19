package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    EditText mailAddress, password;



    private FirebaseAuth firebaseAuth;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mailAddress = (EditText) findViewById(R.id.mailAddress);
        password = (EditText) findViewById(R.id.password);

        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();


        mAuth = FirebaseAuth.getInstance();
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


        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Logged in successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent toMain = new Intent(getApplicationContext(), MainMenu.class);
                            getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(toMain);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        /*
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
        */

    }
}