package com.erenmeric.yuruyus_arkadasim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    User user;
    TextView nameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //nameDisplay = (TextView) findViewById(R.id.nameDisplay);

        //nameDisplay.setText(user.getName() + user.getSurname());

    }
}