package com.zybooks.memorymap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up some pref files
        // Save values in map_pref.xml
        SharedPreferences namedSharedPref = getSharedPreferences("maps_pref", Context.MODE_PRIVATE);
        
        if(!namedSharedPref.contains("Next_Map_Id")){
            SharedPreferences.Editor editor = namedSharedPref.edit();
            editor.putInt("Next_Map_Id", 1);
            editor.apply();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    navController.getGraph())
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}