package com.example.task14.Activity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task14.Classes.ToHelperModel;
import com.example.task14.Classes.UsersDropDwonList;
import com.example.task14.HelperAdapter.ToHelperAdapter;
import com.example.task14.MainActivity;
import com.example.task14.R;
import com.example.task14.UserAdapter.DropDwonListAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth ;

    RecyclerView recyclerView ;
    ArrayList<ToHelperModel> list;
    ToHelperAdapter toHelperAdapter ;
    ArrayList<String> idList;
    public static String LOCATION = "";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menu_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        idList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        list= new ArrayList<>();
        recyclerView = findViewById(R.id.helperRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toHelperAdapter = new ToHelperAdapter(this,list,idList,HomePageActivity.this);
        recyclerView.setAdapter(toHelperAdapter);
        addNav();
        getData();


    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("toHelper")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    list.add(new ToHelperModel(snapshot1.child("fName").getValue(String.class),snapshot1.child("distance").getValue(String.class)
                                    ,snapshot1.child("carColor").getValue(String.class)
                                    ,snapshot1.child("carType").getValue(String.class)
                                    ,snapshot1.child("phoneNumber").getValue(String.class)
                                    ,snapshot1.child("location").getValue(String.class)));
                                    LOCATION = snapshot1.child("location").getValue(String.class);
                                    idList.add(snapshot1.child("userId").getValue(String.class));

                                }
                                toHelperAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Error..."+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    public void addNav(){
        drawerLayout = findViewById(R.id.helperDrawerLayout);
        toolbar = findViewById(R.id.helperToolbar);
        navigationView = findViewById(R.id.helperNav_view);

        setSupportActionBar(toolbar);

        menu_image = findViewById(R.id.helperMenu_image);
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
                Toast.makeText(getApplicationContext(),"Profile Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),HelperProfile.class));

        }

        return true;
    }


}