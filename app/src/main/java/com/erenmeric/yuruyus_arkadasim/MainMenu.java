package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.erenmeric.yuruyus_arkadasim.Fragments.HomeFragment;
import com.erenmeric.yuruyus_arkadasim.Fragments.ProfileFragment;
import com.erenmeric.yuruyus_arkadasim.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {

    private Fragment selectorFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        bottomNavigationView =findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_add:
                        selectorFragment = null;
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                    /*case R.id.nav_heart:
                        selectorFragment = new Heart*/
                }
            }
        });

    }
}