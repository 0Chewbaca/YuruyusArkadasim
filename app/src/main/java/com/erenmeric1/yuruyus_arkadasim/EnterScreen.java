package com.erenmeric1.yuruyus_arkadasim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.erenmeric1.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EnterScreen extends AppCompatActivity {

    private Fragment selectorFragment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();


        if (mUser != null){
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }
    }

    public void signup(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
        finish();
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}