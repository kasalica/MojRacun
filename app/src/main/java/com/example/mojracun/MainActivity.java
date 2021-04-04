package com.example.mojracun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mojracun.database.DataBaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity context = this;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setBackground(null);
        navView.getMenu().getItem(2).setEnabled(false);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_potrosnja, R.id.navigation_racuni)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FloatingActionButton qrFab = this.findViewById(R.id.qrFab);
        qrFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(context, com.example.mojracun.qrScanner.ScannerActivity.class);
                Intent intent = new Intent(context, com.example.mojracun.qrScanner.ScannerActivity.class);
                startActivity(intent);

            }
        });
    }
}
