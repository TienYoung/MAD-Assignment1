package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends ComponentActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private static final String TAG = MainActivity.class.getSimpleName();

    Button goTo2Button = null;
    Button goTo3Button = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_map) {
                    Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    finish();
                }
            }
        });

//        goTo3Button = findViewById(R.id.goTo3Button);
//        goTo2Button = findViewById(R.id.goTo2Button);

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_2) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_2_to_main");
                                Log.d(TAG, "Data received: " + dataReceived);
                                Toast.makeText(MainActivity.this, dataReceived, Toast.LENGTH_LONG).show();
                            }
                        } else if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_3) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_3_to_main");
                                Log.d(TAG, "Data received: " + dataReceived);
                                Toast.makeText(MainActivity.this, dataReceived, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });



        goTo2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("data_from_main_to_2", "Hello from Main");
//                startActivity(intent);
                getContent.launch(intent);
            }
        });


        goTo3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                intent.putExtra("data_from_main_to_3", "Hello from Main");
//                startActivity(intent);
                getContent.launch(intent);
            }
        });

    }
}
