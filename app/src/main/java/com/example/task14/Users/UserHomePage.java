package com.example.task14.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.task14.MainActivity;
import com.example.task14.R;
import com.example.task14.Users.Fragment.GoogleMaps;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserHomePage extends AppCompatActivity {
    Menu optionsMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menu_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

      switchFragment(new GoogleMaps());

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        menu_image = findViewById(R.id.menu_image);
        menu_image.setOnClickListener(this::add_Nav);


    }


    public void add_Nav(View view) {

        Toast.makeText(getApplicationContext(), "Image Click", Toast.LENGTH_SHORT).show();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "menu Selected", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        drawerLayout.openDrawer(Gravity.LEFT);


    }
    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "LogOut", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_profile:
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                finish();
                break;
            case R.id.nav_orders:
                Toast.makeText(getApplicationContext(), "Order Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),OrdersActivity.class));
                finish();
                break;
        }

        return true;
    }

    public void switchFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

}