package com.erenmeric.yuruyus_arkadasim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EnterScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signup(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}