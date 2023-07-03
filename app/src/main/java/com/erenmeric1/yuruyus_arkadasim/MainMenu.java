package com.erenmeric1.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.erenmeric1.yuruyus_arkadasim.Fragments.HomeFragment;
import com.erenmeric1.yuruyus_arkadasim.Fragments.NotificationFragment;
import com.erenmeric1.yuruyus_arkadasim.Fragments.ProfileFragment;
import com.erenmeric1.yuruyus_arkadasim.Fragments.SearchFragment;
import com.erenmeric1.yuruyus_arkadasim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {

    private Fragment selectorFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        bottomNavigationView =findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;

                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_add:
                        selectorFragment = null;
                        Log.d("code1", "Add Button");
                        startActivity(new Intent(getApplicationContext(), PostActivity.class));

                        break;

                    case R.id.nav_profile:
                        getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", "none").apply();
                        selectorFragment = new ProfileFragment();
                        break;


                    case R.id.nav_heart:
                        selectorFragment = new NotificationFragment();
                }

                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }
                return true;
            }
        });

        Bundle intent = getIntent().getExtras();

        if (intent != null){
            String profileId  = intent.getString("publisherId");
            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            intent.clear();
        } else {
            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", "none").apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }


    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle("Do you want exit?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertDialog.show();
    }*/
}