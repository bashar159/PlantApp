package com.example.task14.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task14.Classes.OredersModel;
import com.example.task14.MainActivity;
import com.example.task14.R;
import com.example.task14.UserAdapter.OrderAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter ;

    private ArrayList<OredersModel> ordersModels;

    private FirebaseDatabase database ;
    private DatabaseReference reference ;
    private FirebaseAuth auth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menu_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference("orders")
        .child(auth.getCurrentUser().getUid());

        drawerLayout = findViewById(R.id.orderDrawer);
        toolbar = findViewById(R.id.orderToolbar);
        navigationView = findViewById(R.id.orderNav_view);

        setSupportActionBar(toolbar);

        menu_image = findViewById(R.id.orderMenu_image);
        menu_image.setOnClickListener(this::add_Nav);

        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersModels = new ArrayList<>();
        orderAdapter = new OrderAdapter(getApplicationContext(),ordersModels);
        recyclerView.setAdapter(orderAdapter);

        getData();


    }

    private void add_Nav(View view) {

        Toast.makeText(getApplicationContext(), "Image Click", Toast.LENGTH_SHORT).show();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Menu Selected", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        drawerLayout.openDrawer(Gravity.LEFT);


    }

    public void getData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        ordersModels.add(new OredersModel(
                                snapshot1.child("name").getValue(String.class)
                                ,snapshot1.child("tExperience").getValue(String.class)
                                ,snapshot1.child("lExperience").getValue(String.class)
                        ));
                        orderAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        }

        return true;
    }

}