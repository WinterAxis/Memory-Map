package com.zybooks.memorymap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up some pref files
        // Save values in map_pref.xml
        SharedPreferences namedSharedPref = getSharedPreferences("maps_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = namedSharedPref.edit();
        editor.putInt("Next_Map_Id", 1);
        editor.apply();



        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_map_list, R.id.navigation_map_editor)
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }
}